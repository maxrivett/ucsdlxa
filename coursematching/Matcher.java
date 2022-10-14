package coursematching;
import java.util.ArrayList; 
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.PrintWriter;  // Import the PrintWriter class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

/**
 * The goal of this program is to match courses (and majors)
 * between students for ease of finding a group of people to 
 * study with. 
 * Author: Max Rivett
 * First Created: October 13, 2022
 * Last Updated: October 13, 2022
 */
public class Matcher {

  public static String quarter = "fa22"; // CHANGE THIS EVERY QUARTER
  // remember to make new directories for new quarters too

  public static void main(String[] args) {
    int numStudents = countFileLines();
    /*
     * Array that holds students' info. All info is concatenated into one string with
     * the format - "name:email:major:course1:course2:course3:course4:course5:course6:".
     * Of course, students will not all have 6 courses, most will have 4, and some will
     * have 3 or 5.
     */
    String[] studentInfo = new String[numStudents];
    addStudentInfo(studentInfo);
        
    ArrayList<String> majors = new ArrayList<String>(); // self-explanatory
    ArrayList<String> courses = new ArrayList<String>(); // self-explanatory

    /*
     * Adds majors and courses of all students to the
     * respective arraylists.
     */
    for (int i = 0; i < numStudents; i++) {
      addMajor(studentInfo[i], majors);
      addCourses(studentInfo[i], courses);
    }
    // remove duplicates from each arraylist
    removeDuplicates(majors);
    removeDuplicates(courses);

    // wipe all files clean before adding new text
    for (int i = 0; i < majors.size(); i++) {
      emptyMajorFile(majors.get(i));
    }
    for (int i = 0; i < courses.size(); i++) {
      emptyCourseFile(courses.get(i));
    }

    // iterate through majors/courses and match
    for (int i = 0; i < majors.size(); i++) {
      matchMajor(studentInfo, numStudents, majors.get(i));
    }
    for (int i = 0; i < courses.size(); i++) {
      matchCourse(studentInfo, numStudents, courses.get(i));
    }
  }

  /**
   * Adds a person's major to the major list
   * @param person student's information
   * @param al majors list
   */
  public static void addMajor(String person, ArrayList<String> al) {
    String firstCut = person.substring(person.indexOf(":") + 1,person.length());
    String secondCut = firstCut.substring(firstCut.indexOf(":")+1, firstCut.indexOf(":",firstCut.indexOf(":")+1));
    al.add(secondCut);
  }

  /**
   * Adds a person's courses to the major list
   * @param person student's information
   * @param al courses lis
   */
  public static void addCourses(String person, ArrayList<String> al) {
    String firstCut = person.substring(person.indexOf(":") + 1,person.length());
    String secondCut = firstCut.substring(firstCut.indexOf(":") + 1,firstCut.length());
    String thirdCut = secondCut.substring(secondCut.indexOf(":") + 1,secondCut.length());
    // loop through to add every course of theirs
    while (thirdCut.indexOf(":") >= 0) {
      // overcomplicated if series to catch cases 
      // where students don't have many courses
      if (thirdCut.equals("::::::")) {
        break;
      } else if (thirdCut.equals(":::::")) {
        break;
      } else if (thirdCut.equals("::::")) {
        break;
      } else if (thirdCut.equals(":::")) {
        break;
      } else if (thirdCut.equals("::")) {
        break;
      } else if (thirdCut.equals(":")) {
        break;
      } 
      String tmp = thirdCut.substring(0, thirdCut.indexOf(":"));
      al.add(tmp);
      thirdCut = thirdCut.substring(thirdCut.indexOf(":")+1, thirdCut.length());
    }
  }

  /**
   * Remove all duplicates from an arraylist of
   * strings. Used to remove duplicates from the 
   * majors and courses lists before matching.
   * @param al list to remove duplicates from
   */
  public static void removeDuplicates(ArrayList<String> al) {   
        ArrayList<String> newList = new ArrayList<String>(); // create new list
        for (String ele : al) {  // add unique elements to new list
            if (!newList.contains(ele)) { 
                newList.add(ele); 
            } 
        } 
        al.clear(); // clear old list
        for (String ele : newList) { // copy new list to old list
          al.add(ele);
        }
    } 

    /**
     * Iterates through the list of students and checks
     * if they are of the same major that was passed in
     * as an argument. If so, writes to the major file
     * with their name and email.
     * @param people student info list
     * @param numPeople number of students
     * @param major major being checked
     */
    public static void matchMajor(String[] people, int numPeople, String major) {
      createMajorFile(major);
      for (int i = 0; i < numPeople; i++) {
        String tmp = people[i];
        if (tmp.contains(major)) {
          String text = tmp.substring(0, tmp.indexOf(":")) + " (" + tmp.substring(tmp.indexOf(":")+1, tmp.indexOf(":", tmp.indexOf(":") + 1)) + ")";
          writeToMajorFile(major, text);
        }
      }
    }

    /**
     * Iterates through the list of students and checks
     * if they are of the same course that was passed in
     * as an argument. If so, writes to the course file
     * with their name and email.
     * @param people student info list
     * @param numPeople number of students
     * @param course course being checked
     */
    public static void matchCourse(String[] people, int numPeople, String course) {
      createCourseFile(course);
      for (int i = 0; i < numPeople; i++) {
        String tmp = people[i];
        if (tmp.contains(course)) {
          String text = tmp.substring(0, tmp.indexOf(":")) + " (" + tmp.substring(tmp.indexOf(":")+1, tmp.indexOf(":", tmp.indexOf(":") + 1)) + ")";
          writeToCourseFile(course, text);
        }
      }
    }

    /**
     * Creates a file in the majors directory
     * with the argument as the filename.
     * @param filename name of file
     */
    public static void createMajorFile(String filename) {
      try {
        File myObj = new File("matches/" + quarter + "/majors/" + filename);
        if (myObj.createNewFile()) {
          // System.out.println("File created: " + myObj.getName());
        } else {
          // System.out.println("File already exists.");
        }
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    /**
     * Creates a file in the courses directory
     * with the argument as the filename.
     * @param filename name of file
     */
    public static void createCourseFile(String filename) {
      try {
        File myObj = new File("matches/" + quarter + "/courses/" + filename);
        if (myObj.createNewFile()) {
          // System.out.println("File created: " + myObj.getName());
        } else {
          // System.out.println("File already exists.");
        }
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    /**
     * Writes to a file in the majors directory.
     * Writes the text passed in the argument.
     * @param filename name of file
     * @param text text to write to file
     */
    public static void writeToMajorFile(String filename, String text) {
      try {
        FileWriter fw = new FileWriter("matches/" + quarter + "/majors/" + filename, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(text);
        pw.close();
        fw.close();
        // System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    /**
     * Writes to a file in the courses directory.
     * Writes the text passed in the argument.
     * @param filename name of file
     * @param text text to write to file
     */
    public static void writeToCourseFile(String filename, String text) {
      try {
        FileWriter fw = new FileWriter("matches/" + quarter + "/courses/" + filename, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(text);
        pw.close();
        fw.close();
        // System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    /**
     * Removes all contents from a file
     * in the majors directory.
     * @param filename name of file
     */
    public static void emptyMajorFile(String filename) {
      try {
        FileWriter fw = new FileWriter("matches/" + quarter + "/majors/" + filename, false);
        PrintWriter pw = new PrintWriter(fw);
        pw.print("");
        pw.close();
        fw.close();
        // System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    /**
     * Removes all contents from a file
     * in the courses directory.
     * @param filename name of file
     */
    public static void emptyCourseFile(String filename) {
      try {
        FileWriter fw = new FileWriter("matches/" + quarter + "/courses/" + filename, false);
        PrintWriter pw = new PrintWriter(fw);
        pw.print("");
        pw.close();
        fw.close();
        // System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    /**
     * Reads the input file (raw.txt) and
     * counts how many lines there are.
     * @return the number of lines
     */
    public static int countFileLines() {
      int cnt = 0;
      try {
        File myObj = new File("matches/" + quarter + "/raw.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
          myReader.nextLine();
          cnt++;
        }
        myReader.close();
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
      return cnt;
    }

    /**
     * Reads the input file (raw.txt) and adds
     * the student information to the array.
     * @param people array of student info
     */
    public static void addStudentInfo(String[] people) {
      try {
        int ctr = 0;
        File myObj = new File("matches/" + quarter + "/raw.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          String edited = data.replaceAll("/", "");
          people[ctr] = edited;
          ctr++;
        }
        myReader.close();
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

}