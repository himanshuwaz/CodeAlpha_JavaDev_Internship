import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The Chatbot class implements a simple rule-based AI for interactive communication.
 * It can respond to predefined questions and learn new responses.
 */
public class Chatbot {

    // A map to store predefined rules: keyword sets mapped to responses.
    // Using a Set<String> for keywords allows for flexible matching regardless of order.
    private Map<Set<String>, String> knowledgeBase;
    private Scanner scanner;

    /**
     * Constructor for the Chatbot. Initializes the knowledge base with some default FAQs.
     */
    public Chatbot() {
        knowledgeBase = new HashMap<>();
        scanner = new Scanner(System.in);
        initializeKnowledgeBase();
    }

    /**
     * Initializes the chatbot's knowledge base with some common questions and answers.
     * Each entry maps a set of keywords (representing a question) to a predefined answer.
     */
    private void initializeKnowledgeBase() {
        // Greetings
        knowledgeBase.put(new HashSet<>(Arrays.asList("hello", "hi", "hey")), "Hello there! How can I help you today?");
        knowledgeBase.put(new HashSet<>(Arrays.asList("how", "are", "you")), "I'm a bot, so I don't have feelings, but I'm ready to assist!");

        // Internship related questions
        knowledgeBase.put(new HashSet<>(Arrays.asList("internship", "program", "details")), "Our internship programs cover various domains like Web Development, App Development, Python, and Java. What are you interested in?");
        knowledgeBase.put(new HashSet<>(Arrays.asList("duration", "internship")), "The internship duration is typically 1 or 2 months.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("stipend")), "Stipend details vary per program. Please refer to the specific program details.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("start", "date", "internship")), "The general start date for internships is 05/11/2023, but new batches might open.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("tasks", "complete")), "You need to complete 2 or 3 out of the 4 assigned tasks for your domain.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("certificate", "internship")), "Yes, upon successful completion of the required tasks, you will receive a completion certificate.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("github", "repo")), "You should upload your complete source code to GitHub in a repository named 'CodeAlpha_ProjectName'.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("submission", "form")), "The submission form will be shared in your WhatsApp group.");

        // General questions
        knowledgeBase.put(new HashSet<>(Arrays.asList("your", "name")), "I am a simple rule-based chatbot.");
        knowledgeBase.put(new HashSet<>(Arrays.asList("thank", "you")), "You're welcome! Is there anything else I can help with?");
        knowledgeBase.put(new HashSet<>(Arrays.asList("bye", "goodbye")), "Goodbye! Have a great day!");
    }

    /**
     * Processes the user's input and generates a response.
     * It tries to find a matching set of keywords in the knowledge base.
     * If no direct match, it provides a generic response or asks to learn.
     * @param userInput The raw input string from the user.
     * @return The chatbot's response.
     */
    public String getResponse(String userInput) {
        // Normalize the input: convert to lowercase and split into words
        String[] words = userInput.toLowerCase().split("\\W+"); // Split by non-word characters
        Set<String> inputKeywords = new HashSet<>(Arrays.asList(words));

        // Try to find a match in the knowledge base
        for (Map.Entry<Set<String>, String> entry : knowledgeBase.entrySet()) {
            Set<String> storedKeywords = entry.getKey();
            String response = entry.getValue();

            // Check if all stored keywords are present in the user's input
            // This is a simple form of keyword matching (NLP technique)
            if (inputKeywords.containsAll(storedKeywords) && !storedKeywords.isEmpty()) {
                return response;
            }
        }

        // If no direct match, try a partial match or provide a default response
        for (Map.Entry<Set<String>, String> entry : knowledgeBase.entrySet()) {
            Set<String> storedKeywords = entry.getKey();
            String response = entry.getValue();

            // Check for any overlap in keywords
            Set<String> intersection = new HashSet<>(inputKeywords);
            intersection.retainAll(storedKeywords); // Keep only common elements

            if (!intersection.isEmpty() && intersection.size() >= (storedKeywords.size() / 2.0)) { // Match at least half of the keywords
                return "I think you're asking about " + String.join(", ", intersection) + ". " + response;
            }
        }


        // If still no match, offer to learn
        return "I'm not sure how to respond to that. Can you rephrase or teach me? (Type 'teach' to add a new response)";
    }

    /**
     * Allows the user to teach the chatbot a new question-answer pair.
     */
    public void teach() {
        System.out.println("\n--- Teach Me ---");
        System.out.print("What question or keywords should I listen for? (e.g., 'how to submit', 'certificate'): ");
        String questionInput = scanner.nextLine();
        Set<String> newKeywords = new HashSet<>(Arrays.asList(questionInput.toLowerCase().split("\\W+")));

        if (newKeywords.isEmpty()) {
            System.out.println("No keywords provided. Teaching cancelled.");
            return;
        }

        // Check if these keywords already exist in the knowledge base
        for (Set<String> existingKeywords : knowledgeBase.keySet()) {
            if (existingKeywords.equals(newKeywords)) {
                System.out.println("I already have a response for these keywords. Teaching cancelled.");
                return;
            }
        }

        System.out.print("What should my response be? ");
        String newResponse = scanner.nextLine();

        if (newResponse.trim().isEmpty()) {
            System.out.println("Response cannot be empty. Teaching cancelled.");
            return;
        }

        knowledgeBase.put(newKeywords, newResponse);
        System.out.println("Thank you! I've learned a new response.");
    }

    /**
     * Displays the main menu for the chatbot.
     */
    public void displayMenu() {
        System.out.println("\n--- Chatbot Menu ---");
        System.out.println("Type your message to chat.");
        System.out.println("Type 'teach' to teach me a new response.");
        System.out.println("Type 'exit' to quit.");
        System.out.print("You: ");
    }

    /**
     * Main method to run the Chatbot application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Chatbot bot = new Chatbot();
        String userInput;

        System.out.println("Welcome to the CodeAlpha Internship Chatbot!");
        System.out.println("Ask me anything about the internship or general queries.");

        while (true) {
            bot.displayMenu();
            userInput = bot.scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Chatbot: Goodbye! Thanks for chatting.");
                break;
            } else if (userInput.equalsIgnoreCase("teach")) {
                bot.teach();
            } else {
                String response = bot.getResponse(userInput);
                System.out.println("Chatbot: " + response);
            }
        }
        bot.scanner.close(); // Close the scanner when done
    }
}
