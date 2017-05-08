package com.csc301.team7.era;

import com.android.volley.RequestQueue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Spas on 2017-03-25.
 */

public class Diagnosis {
    public static DiagnosisClient _diagnosisClient;
    public Diagnosis(RequestQueue queue){
        String authUrl = "";
        String userName = "";
        String password = "";
        String language = "";
        String healthUrl = "";

        Properties prop = new Properties();
        InputStream input = null;
        //userName = "spasimir95@gmail.com";   //these are just for testing
        //password = "Gt45MzPb96ReCc27W";
        //authUrl = "https://sandbox-authservice.priaid.ch/login";
        //healthUrl = "https://sandbox-healthservice.priaid.ch";
        //language = "en-gb";

        userName = "spasimir_vasilev";  //these are the real ones
        password = "K2F98#nS65tqA-wK";
        authUrl = "https://authservice.priaid.ch/login";
        healthUrl = "https://healthservice.priaid.ch";
        language = "en-gb";

        CheckRequiredArgs(userName, password, authUrl, healthUrl, language);

        try {
            _diagnosisClient = new DiagnosisClient(userName, password, authUrl, language, healthUrl, queue);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    static void CheckRequiredArgs(String userName, String password, String authUrl, String healthUrl, String language) {
        if (userName == null || userName.isEmpty())
            throw new IllegalArgumentException("username in config.properties is required");

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("password in config.properties is required");

        if (authUrl == null || authUrl.isEmpty())
            throw new IllegalArgumentException("priaid_authservice_url in config.properties is required");

        if (healthUrl == null || healthUrl.isEmpty())
            throw new IllegalArgumentException("priaid_healthservice_url in config.properties is required");

        if (language == null || language.isEmpty())
            throw new IllegalArgumentException("language in config.properties is required");
    }

    static int GetRandom(int maxNumber) {
        return ThreadLocalRandom.current().nextInt(0, maxNumber);
    }

    static void writeHeaderMessage(String message) {
        System.out.println("---------------------------------------------");
        System.out.println(message);
        System.out.println("---------------------------------------------");
    }

    public int loadBodySublocations(int locId) throws Exception {
        List<HealthItem> bodySublocations = _diagnosisClient.loadBodySubLocations(locId);

        if (bodySublocations == null || bodySublocations.size() == 0)
            throw new Exception("Empty body sublocations results");

        for (HealthItem loc : bodySublocations)
            System.out.println(loc.Name + " " + loc.ID);

        int randomLocIndex = GetRandom(bodySublocations.size());
        HealthItem randomLocation = bodySublocations.get(randomLocIndex);

        writeHeaderMessage("Sublocations symptoms for selected location " + randomLocation.Name);

        return randomLocation.ID;
    }

    public Integer loadBodyLocations() throws Exception {
        List<HealthItem> bodyLocations = _diagnosisClient.loadBodyLocations();

        if (bodyLocations == null || bodyLocations.size() == 0)
            throw new Exception("Empty body locations results");

        writeHeaderMessage("Body locations:");

        for (HealthItem loc : bodyLocations)
            System.out.println(loc.Name + " (" + loc.ID + ")");

        int randomLocIndex = GetRandom(bodyLocations.size());
        HealthItem randomLocation = bodyLocations.get(randomLocIndex);

        writeHeaderMessage("Sublocations for randomly selected location " + randomLocation.Name);

        return randomLocation.ID;
    }

    static List<HealthSymptomSelector> LoadSublocationSymptoms(int subLocId) throws Exception {
        List<HealthSymptomSelector> symptoms = _diagnosisClient.loadSublocationSymptoms(subLocId, SelectorStatus.Man);

        if (symptoms == null || symptoms.size() == 0) {
            System.out.println("Empty body sublocations symptoms results");
            return null;
        }

        writeHeaderMessage("Body sublocations symptoms:");

        for (HealthSymptomSelector sym : symptoms)
            System.out.println(sym.Name);

        int randomSymptomIndex = GetRandom(symptoms.size());

        randomSymptomIndex = GetRandom(symptoms.size());

        HealthSymptomSelector randomSymptom = symptoms.get(randomSymptomIndex);

        writeHeaderMessage("Randomly selected symptom: " + randomSymptom.Name);

        List<HealthSymptomSelector> selectedSymptoms = new ArrayList<HealthSymptomSelector>();
        selectedSymptoms.add(randomSymptom);

        LoadRedFlag(randomSymptom);

        return selectedSymptoms;
    }

    static List<Integer> LoadDiagnosis(List<HealthSymptomSelector> selectedSymptoms) throws Exception {
        writeHeaderMessage("Diagnosis");

        List<Integer> selectedSymptomsIds = new ArrayList<Integer>();
        for (HealthSymptomSelector symptom : selectedSymptoms) {
            selectedSymptomsIds.add(symptom.ID);
        }

        List<HealthDiagnosis> diagnosis = _diagnosisClient.loadDiagnosis(selectedSymptomsIds, Gender.Male, 1988);

        if (diagnosis == null || diagnosis.size() == 0) {
            writeHeaderMessage("No diagnosis results for symptom " + selectedSymptoms.get(0).Name);
            return null;
        }

        for (HealthDiagnosis d : diagnosis) {
            String specialistions = "";
            for (MatchedSpecialisation spec : d.Specialisation)
                specialistions = specialistions.concat(spec.Name + ", ");
            System.out.println(d.Issue.Name + " - " + d.Issue.Accuracy + "% \nSpecialisations : " + specialistions);
        }

        List<Integer> retValue = new ArrayList<Integer>();
        for (HealthDiagnosis diagnose : diagnosis)
            retValue.add(diagnose.Issue.ID);
        return retValue;
    }


    static void LoadSpecialisations(List<HealthSymptomSelector> selectedSymptoms) throws Exception {
        writeHeaderMessage("Specialisations");

        List<Integer> selectedSymptomsIds = new ArrayList<Integer>();
        for (HealthSymptomSelector symptom : selectedSymptoms) {
            selectedSymptomsIds.add(symptom.ID);
        }

        List<DiagnosedIssue> specialisations = _diagnosisClient.loadSpecialisations(selectedSymptomsIds, Gender.Male, 1988);

        if (specialisations == null || specialisations.size() == 0) {
            writeHeaderMessage("No specialisations for symptom " + selectedSymptoms.get(0).Name);
            return;
        }

        for (DiagnosedIssue s : specialisations)
            System.out.println(s.Name + " - " + s.Accuracy + "%");
    }


    static void LoadRedFlag(HealthSymptomSelector selectedSymptom) throws Exception {
        String redFlag = "Symptom " + selectedSymptom.Name + " has no red flag";

        if (selectedSymptom.HasRedFlag)
            redFlag = _diagnosisClient.loadRedFlag(selectedSymptom.ID);

        writeHeaderMessage(redFlag);
    }

    static void LoadIssueInfo(int issueId) throws Exception {
        HealthIssueInfo issueInfo = _diagnosisClient.loadIssueInfo(issueId);
        writeHeaderMessage("Issue info");
        System.out.println("Name: " + issueInfo.Name);
        System.out.println("Professional Name: " + issueInfo.ProfName);
        System.out.println("Synonyms: " + issueInfo.Synonyms);
        System.out.println("Short Description: " + issueInfo.DescriptionShort);
        System.out.println("Description: " + issueInfo.Description);
        System.out.println("Medical Condition: " + issueInfo.MedicalCondition);
        System.out.println("Treatment Description: " + issueInfo.TreatmentDescription);
        System.out.println("Possible symptoms: " + issueInfo.PossibleSymptoms + "\n");
    }

    static void LoadProposedSymptoms(List<HealthSymptomSelector> selectedSymptoms) throws Exception {
        List<Integer> selectedSymptomsIds = new ArrayList<Integer>();
        for (HealthSymptomSelector symptom : selectedSymptoms) {
            selectedSymptomsIds.add(symptom.ID);
        }
        List<HealthItem> proposedSymptoms = _diagnosisClient.loadProposedSymptoms(selectedSymptomsIds, Gender.Male, 1988);

        if (proposedSymptoms == null || proposedSymptoms.size() == 0) {
            writeHeaderMessage("No proposed symptoms for selected symptom " + selectedSymptoms.get(0).Name);
            return;
        }

        String proposed = "";
        for (HealthItem diagnose : proposedSymptoms)
            proposed = proposed.concat(diagnose.Name) + ", ";

        writeHeaderMessage("Proposed symptoms: " + proposed);
    }

}
