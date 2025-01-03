package com.example.quiz.data.datasources.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 6;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ROLES = "roles";
    private static final String TABLE_USER_ROLES = "user_roles";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_QUIZ = "quiz";
    private static final String TABLE_USER_QUIZ = "user_quiz";
    private static final String TABLE_QUESTION_QUIZ = "question_quiz";

    // Common Column Names
    private static final String COLUMN_ID = "id";

    // Roles Table Columns
    private static final String COLUMN_ROLE_NAME = "role_name";

    // Users Table Columns
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // User-Roles Table Columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_ROLE_ID = "role_id";

    // Questions Table Columns
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_OPTION_1 = "option_1";
    private static final String COLUMN_OPTION_2 = "option_2";
    private static final String COLUMN_OPTION_3 = "option_3";
    private static final String COLUMN_OPTION_4 = "option_4";
    private static final String COLUMN_OPTION_5 = "option_5";
    private static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    private static final String COLUMN_NUMBER_OF_POINTS = "number_of_points";

    // Quiz Table Columns
    private static final String COLUMN_DATE = "date";
    private  static final String COLUMN_QUIZ_NAME = "quiz_name";

    // User-Quiz Table Columns
    private static final String COLUMN_QUIZ_ID = "quiz_id";
    private static final String COLUMN_TOTAL_NUMBER_OF_POINTS = "total_number_of_points";

    // Question-Quiz Table Columns
    private static final String COLUMN_QUESTION_ID = "question_id";

    // Table Creation Statements
    private static final String CREATE_TABLE_ROLES = "CREATE TABLE " + TABLE_ROLES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ROLE_NAME + " TEXT NOT NULL UNIQUE);";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
            COLUMN_LAST_NAME + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_USER_ROLES = "CREATE TABLE " + TABLE_USER_ROLES + " (" +
            COLUMN_USER_ID + " INTEGER NOT NULL, " +
            COLUMN_ROLE_ID + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_ROLE_ID + "), " +
            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_ROLE_ID + ") REFERENCES " + TABLE_ROLES + "(" + COLUMN_ID + "));";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TEXT + " TEXT NOT NULL, " +
            COLUMN_OPTION_1 + " TEXT NOT NULL, " +
            COLUMN_OPTION_2 + " TEXT NOT NULL, " +
            COLUMN_OPTION_3 + " TEXT NOT NULL, " +
            COLUMN_OPTION_4 + " TEXT NOT NULL, " +
            COLUMN_OPTION_5 + " TEXT, " +
            COLUMN_CORRECT_ANSWER + " TEXT NOT NULL, " +
            COLUMN_NUMBER_OF_POINTS + " INTEGER NOT NULL);";

    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE " + TABLE_QUIZ + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_QUIZ_NAME + " TEXT NOT NULL, " +
            COLUMN_DATE + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_USER_QUIZ = "CREATE TABLE " + TABLE_USER_QUIZ + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " INTEGER NOT NULL, " +
            COLUMN_QUIZ_ID + " INTEGER NOT NULL, " +
            COLUMN_TOTAL_NUMBER_OF_POINTS + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_QUIZ_ID + ") REFERENCES " + TABLE_QUIZ + "(" + COLUMN_ID + "));";


    private static final String CREATE_TABLE_QUESTION_QUIZ = "CREATE TABLE " + TABLE_QUESTION_QUIZ + " (" +
            COLUMN_QUESTION_ID + " INTEGER NOT NULL, " +
            COLUMN_QUIZ_ID + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + COLUMN_QUESTION_ID + ", " + COLUMN_QUIZ_ID + "), " +
            "FOREIGN KEY (" + COLUMN_QUESTION_ID + ") REFERENCES " + TABLE_QUESTIONS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_QUIZ_ID + ") REFERENCES " + TABLE_QUIZ + "(" + COLUMN_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
        db.execSQL(CREATE_TABLE_ROLES);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_USER_ROLES);
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_QUIZ);
        db.execSQL(CREATE_TABLE_USER_QUIZ);
        db.execSQL(CREATE_TABLE_QUESTION_QUIZ);

        // Insert default roles
        db.execSQL("INSERT INTO " + TABLE_ROLES + " (" + COLUMN_ROLE_NAME + ") VALUES ('Admin');");
        db.execSQL("INSERT INTO " + TABLE_ROLES + " (" + COLUMN_ROLE_NAME + ") VALUES ('User');");

        // Insert default admin user
        String defaultAdminEmail = "admin@example.com";
        String defaultAdminPassword = "adminpassword";
        db.execSQL("INSERT INTO " + TABLE_USERS + " (" +
                COLUMN_FIRST_NAME + ", " +
                COLUMN_LAST_NAME + ", " +
                COLUMN_EMAIL + ", " +
                COLUMN_PASSWORD + ") VALUES ('Admin', 'User', '" +
                defaultAdminEmail + "', '" +
                defaultAdminPassword + "');");

        // Get Admin User ID
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + " = ?", new String[]{defaultAdminEmail});
        if (cursor.moveToFirst()) {
            int adminUserIdIndex = cursor.getColumnIndex(COLUMN_ID);
            if (adminUserIdIndex != -1) {
                int adminUserId = cursor.getInt(adminUserIdIndex);
                cursor.close();

                // Get Admin Role ID
                cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_ROLES +
                        " WHERE " + COLUMN_ROLE_NAME + " = ?", new String[]{"Admin"});
                if (cursor.moveToFirst()) {
                    int adminRoleIdIndex = cursor.getColumnIndex(COLUMN_ID);
                    if (adminRoleIdIndex != -1) {
                        int adminRoleId = cursor.getInt(adminRoleIdIndex);
                        cursor.close();

                        // Assign Admin Role To Admin User
                        db.execSQL("INSERT INTO " + TABLE_USER_ROLES + " (" +
                                COLUMN_USER_ID + ", " +
                                COLUMN_ROLE_ID + ") VALUES (" +
                                adminUserId + ", " +
                                adminRoleId + ");");
                    }
                }
            }
        }

        String[][] questions = {
                {"What is the capital of France?", "Rome", "Paris", "London", "Madrid", "Berlin", "Paris", "1"},
                {"Who wrote 'To Kill a Mockingbird'?", "Harper Lee", "Mark Twain", "J.K. Rowling", "Ernest Hemingway", "Jane Austen", "Harper Lee", "1"},
                {"What is the largest planet in our solar system?", "Mars", "Venus", "Jupiter", "Earth", "Saturn", "Jupiter", "2"},
                {"What is the chemical symbol for gold?", "Hg", "Fe", "Ag", "Pb", "Au", "Au", "3"},
                {"Who painted the Mona Lisa?", "Pablo Picasso", "Leonardo da Vinci", "Vincent van Gogh", "Claude Monet", "Rembrandt", "Leonardo da Vinci", "5"},
                {"What is the square root of 64?", "10", "7", "6", "8", "9", "8", "8"},
                {"Who discovered penicillin?", "Alexander Fleming", "Albert Einstein", "Marie Curie", "Isaac Newton", "Louis Pasteur", "Alexander Fleming", "13"},
                {"What is the longest river in the world?", "Yangtze", "Mississippi", "Amazon", "Nile", "Ganges", "Nile", "21"},
                {"Who was the first President of the United States?", "Abraham Lincoln", "George Washington", "Thomas Jefferson", "John Adams", "James Madison", "George Washington", "34"},
                {"What is the chemical formula for water?", "NaCl", "H2SO4", "O2", "CO2", "H2O", "H2O", "55"},
                {"What is the smallest prime number?", "1", "3", "5", "2", "7", "2", "89"},
                {"Which country has the largest population?", "India", "USA", "China", "Russia", "Brazil", "China", "144"},
                {"What is the freezing point of water in Celsius?", "32", "0", "-273", "100", "-40", "0", "233"},
                {"Who developed the theory of relativity?", "Albert Einstein", "Isaac Newton", "Marie Curie", "Galileo Galilei", "Stephen Hawking", "Albert Einstein", "377"},
                {"What is the hardest natural substance on Earth?", "Diamond", "Gold", "Iron", "Platinum", "Graphite", "Diamond", "610"}
        };

        for (String[] questionData : questions) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEXT, questionData[0]);
            values.put(COLUMN_OPTION_1, questionData[1]);
            values.put(COLUMN_OPTION_2, questionData[2]);
            values.put(COLUMN_OPTION_3, questionData[3]);
            values.put(COLUMN_OPTION_4, questionData[4]);
            values.put(COLUMN_OPTION_5, questionData[5]);
            values.put(COLUMN_CORRECT_ANSWER, questionData[6]);
            values.put(COLUMN_NUMBER_OF_POINTS, Integer.parseInt(questionData[7]));
            db.insert(TABLE_QUESTIONS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ROLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES);
        onCreate(db);
    }
}