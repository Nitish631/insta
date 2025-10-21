package com.nitish.insta.Configuration;

import java.util.*;

import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConstant {
    public static final Integer USER_ROLE_ID = 999;
    public static final Integer ADMIN_ROLE_ID = 222;
    public static final long JWT_TOKEN_VALIDITY = 15 * 60 * 1000;
    public static final String JWT_SECRET_KEY = "klkaiuyyjhqbjeguayyggjhhajbhaljfgfauygffjhhjhbjf";
    public static final String APP_MAME = "Instagram";

    // =========================
    // MAIN CATEGORIES
    // =========================
    public static final Map<Integer, String> MAIN_CATEGORIES = new HashMap<>() {
        {
            put(1, "Technology");
            put(2, "Education");
            put(3, "Entertainment");
            put(4, "Sports");
            put(5, "Lifestyle");
            put(6, "Travel");
            put(7, "Gaming");
            put(8, "Food");
            put(9, "Art & Design");
            put(10, "News & Politics");
            put(11, "Business");
            put(12, "Science");
            put(13, "Music");
            put(14, "Health & Fitness");
            put(15, "Fashion");
            put(16, "Relationships & Emotions");
        }
    };

    // =========================
    // SUB CATEGORIES (Expanded + Updated Entertainment)
    // =========================
    public static final Map<Integer, String> SUB_CATEGORIES = new HashMap<>() {
        {
            // Technology
            put(1, "Programming");
            put(2, "Artificial Intelligence");
            put(3, "Machine Learning");
            put(4, "Mobile Development");
            put(5, "Web Development");
            put(6, "Data Science");
            put(7, "Cybersecurity");
            put(8, "Cloud Computing");
            put(9, "Tech Reviews");
            put(10, "Gadgets");
            put(11, "Blockchain");
            put(12, "IoT Development");
            put(13, "Robotics");

            // Education
            put(14, "Physics");
            put(15, "Chemistry");
            put(16, "Mathematics");
            put(17, "Biology");
            put(18, "Language Learning");
            put(19, "Creative Writing");
            put(20, "Public Speaking");
            put(21, "Motivational Talks");

            // Entertainment (You missed these earlier)
            put(22, "Comedy");
            put(23, "Movies & Reviews");
            put(24, "Memes");
            put(25, "Reels & Shorts");
            put(26, "Dance");
            put(27, "Challenges");
            put(28, "Streaming Clips");
            put(29, "Behind the Scenes");
            put(30, "Reaction Videos");
            put(31, "Storytelling");

            // Sports
            put(32, "Football");
            put(33, "Cricket");
            put(34, "Basketball");
            put(35, "E-sports");
            put(36, "Fitness Challenges");

            // Lifestyle
            put(37, "Daily Vlogs");
            put(38, "Minimalism");
            put(39, "Mindfulness");
            put(40, "Personal Growth");

            // Travel
            put(41, "Travel Vlogs");
            put(42, "Adventure Travel");
            put(43, "Cultural Exploration");

            // Gaming
            put(44, "Game Reviews");
            put(45, "Gameplay Highlights");
            put(46, "Streaming Clips");

            // Food
            put(47, "Cooking");
            put(48, "Street Food");
            put(49, "Food Reviews");
            put(50, "Healthy Recipes");

            // Art & Design
            put(51, "Photography");
            put(52, "Graphic Design");
            put(53, "UI/UX Design");
            put(54, "Digital Painting");
            put(55, "Architecture Design");

            // News & Politics
            put(56, "Global News");
            put(57, "Local News");
            put(58, "Debates");
            put(59, "Interviews");

            // Business
            put(60, "Startups");
            put(61, "Finance");
            put(62, "Stock Market");
            put(63, "Entrepreneurship");
            put(64, "Economics");

            // Science
            put(65, "Space Science");
            put(66, "Medical Innovations");
            put(67, "Astrophysics");
            put(68, "Nanotechnology");
            put(69, "Environmental Science");

            // Music
            put(70, "Music Covers");
            put(71, "Instrumental");
            put(72, "Singing Reels");
            put(73, "Live Performances");
            put(74, "Music Production");
            put(75, "DJ Mixes");

            // Health & Fitness
            put(76, "Workout Tutorials");
            put(77, "Diet Plans");
            put(78, "Yoga");
            put(79, "Mental Health");
            put(80, "Wellness Tips");

            // Fashion
            put(81, "Fashion Trends");
            put(82, "Outfit Ideas");
            put(83, "Makeup Tutorials");
            put(84, "Style Tips");
            // FRIENDSHIP AND RELATIONSHIPS AND EMOTIONS
            put(85, "Friendships");
            put(86, "Romantic Relationships");
            put(87, "Family Bonds");
            put(90, "Positivity & Mindset");
            put(88, "Self-Love & Confidence");
            put(89, "Life Lessons");
            put(90, "Positivity & Mindset");
        }
    };

    // =========================
    // MAIN → SUB RELATION MAP
    // =========================
    public static final Map<Integer, List<Integer>> MAIN_TO_SUB_MAP = new HashMap<>() {
        {
            put(1, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)); // Technology
            put(2, Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21)); // Education
            put(3, Arrays.asList(22, 23, 24, 25, 26, 27, 28, 29, 30, 31)); // Entertainment
            put(4, Arrays.asList(32, 33, 34, 35, 36)); // Sports
            put(5, Arrays.asList(37, 38, 39, 40)); // Lifestyle
            put(6, Arrays.asList(41, 42, 43)); // Travel
            put(7, Arrays.asList(44, 45, 46)); // Gaming
            put(8, Arrays.asList(47, 48, 49, 50)); // Food
            put(9, Arrays.asList(51, 52, 53, 54, 55)); // Art & Design
            put(10, Arrays.asList(56, 57, 58, 59)); // News & Politics
            put(11, Arrays.asList(60, 61, 62, 63, 64)); // Business
            put(12, Arrays.asList(65, 66, 67, 68, 69)); // Science
            put(13, Arrays.asList(70, 71, 72, 73, 74, 75)); // Music
            put(14, Arrays.asList(76, 77, 78, 79, 80)); // Health & Fitness
            put(15, Arrays.asList(81, 82, 83, 84)); // Fashion
            put(16, Arrays.asList(85, 86, 87, 88, 89, 90));//RELATIONSHIP AND EMOTIONS
        }
    };

    // =========================
    // MAIN BAD CATEGORIES
    // =========================
    public static final Map<Integer, String> MAIN_BAD_CATEGORIES = new HashMap<>() {
        {
            put(1, "Sexual Content");
            put(2, "Harassment");
            put(3, "Bullying");
            put(4, "Personal Damage");
            put(5, "Violence");
            put(6, "Hate Speech");
            put(7, "Drug Promotion");
            put(8, "Self-harm");
            put(9, "Graphic Content");
        }
    };

    // =========================
    // SUB BAD CATEGORIES
    // =========================
    public static final Map<Integer, String> SUB_BAD_CATEGORIES = new HashMap<>() {
        {
            put(1, "Explicit Nudity");
            put(2, "Pornographic Acts");
            put(3, "Sexual Harassment");
            put(4, "Verbal Abuse");
            put(5, "Cyberbullying");
            put(6, "Character Defamation");
            put(7, "Threats or Intimidation");
            put(8, "Violent Videos");
            put(9, "Physical Assaults");
            put(10, "Hate Speech Against Groups");
            put(11, "Promotion of Drugs");
            put(12, "Encouraging Suicide");
            put(13, "Self-harm Challenges");
            put(14, "Animal Abuse");
            put(15, "Terrorism Content");
        }
    };

    // =========================
    // MAIN BAD → SUB BAD RELATION
    // =========================
    public static final Map<Integer, List<Integer>> MAIN_BAD_TO_SUB_BAD_MAP = new HashMap<>() {
        {
            put(1, Arrays.asList(1, 2)); // Sexual Content
            put(2, Arrays.asList(3, 4)); // Harassment
            put(3, Arrays.asList(5, 6)); // Bullying
            put(4, Arrays.asList(7)); // Personal Damage
            put(5, Arrays.asList(8, 9, 14)); // Violence
            put(6, Arrays.asList(10, 15)); // Hate Speech
            put(7, Arrays.asList(11)); // Drug Promotion
            put(8, Arrays.asList(12, 13)); // Self-harm
            put(9, Arrays.asList(14, 15)); // Graphic Content
        }
    };
}
