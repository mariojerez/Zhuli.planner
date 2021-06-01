import java.util.*;
import java.time.*;

public class ZhuliUser {
    
    // instance variables
    public String name;
    public String username;
    private String password;
    public Boolean isBusy;
    public int id;
    public static int nextID = 1001;
    public Heap tasks;
    private Boolean passwordCreated;
    // instance variables for scheduling
    public DayOfWeek[] daysOfWeekArray = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
    public Hashtable<DayOfWeek, Double> day_hoursHash;
    public String workerType;
    public Boolean workerTypeSet;
    
    
    //constructor
    public ZhuliUser () {
        // create id
        this.id = nextID;
        nextID++;
        this.workerTypeSet = false;
        this.workerType = "Adriana";
        // create username
        Scanner keyboard = new Scanner(System.in);
        System.out.print("choose a username ");
        this.username = keyboard.nextLine();
        System.out.print("What should I call you? ");
        this.name = keyboard.nextLine();
        // create password
        this.passwordCreated = false;
        while (!passwordCreated) {
            System.out.print("Create your password ");
            String tempPassword = keyboard.nextLine();
            System.out.print("re-enter your password ");
            String reEnteredPassword = keyboard.nextLine();  
            if (reEnteredPassword.equals(tempPassword)) {
                System.out.println("Passwords match");
                this.password = tempPassword;
                this.passwordCreated = true;
            }
            else {
                System.out.println("Sorry, the passwords didn't match. Please try again.");
            }
        }
        // user is not busy
        this.isBusy = false;
        // create empty heap of tasks
        tasks = new Heap();
        day_hoursHash = new Hashtable<DayOfWeek, Double>(7); // capacity of 7
        for (DayOfWeek dayOfWeek : daysOfWeekArray) {
            day_hoursHash.put(dayOfWeek, 6.);
        }
        keyboard.close();
    }
    
    public String toString() {
         return String.format("Name: %d   user name: %s   type of worker: %s", this.name, this.username, this.workerType);
    }
    
    public ArrayList<Task> getTasks() {
        return this.tasks.getOrderedArray();
    }
    
    private Boolean correctPassword(String passwordAttempt) {
        if (passwordAttempt.equals(this.password)) {
            return true;
        }
        return false;
    }
    
    public void addTask(Task newTask) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("please enter your password ");
        String passwordAttempt = keyboard.nextLine();
        if (!correctPassword(passwordAttempt)) {
            System.out.println("AAAAAAAAAAHHHHH INTRUDER INTRUDER INTRUDER!!!! D:");
            return;
        }
        this.tasks.add(newTask);
        System.out.printf("%s was added to your tasks.\n", newTask);
    }
    
    public Double getHoursLeftInWeek(LocalDate today) {
        DayOfWeek todaysDayOfWeek = today.getDayOfWeek();
        Double totalHours = 0.;
        Boolean active = false; // when true we begin to count the rest of the week
        for (int i = 0; i < this.daysOfWeekArray.length; i++) {
            Object day = this.daysOfWeekArray[i];
            if (!active) {
                if (day == todaysDayOfWeek) {
                    active = true;
                }
            }
            if (active) {
                totalHours += this.day_hoursHash.get(day);
            }
        }
        return totalHours;
    }
    
    public Double getMaxWorkHours(DayOfWeek day) {
        return this.day_hoursHash.get(day);
    }
            
        
    public void setPreferences() {
        Scanner keyboard = new Scanner(System.in);
        for (int i = 0; i < this.daysOfWeekArray.length; i++) {
            Object day = this.daysOfWeekArray[i];
            System.out.printf("Your preference is to work a maximum of %f hours on %ss. keep or change?\n", day_hoursHash.get(day), day);
            String ans = keyboard.nextLine();
            if (ans.equals("change")) {
                System.out.printf("How many hours do you want to work on %ss (max)?\n", day);
                String hrs = keyboard.nextLine();
                Double workHrs = Integer.parseInt(hrs) / 1.;
                DayOfWeek dayOfWk = (DayOfWeek) day;
                this.day_hoursHash.replace(dayOfWk, this.day_hoursHash.get(day), workHrs); // change hrs worked that day
                System.out.printf("Ok. You will work %f hours max!\n",day_hoursHash.get(day)); 
            }
            else if (ans.equals("keep")) {
                System.out.printf("Ok. Your %ss will stay the same.\n", day);
            }
        }
        System.out.println("What kind of worker are you? (Maybe a better question is what worker do you want to be?) Are you a...\n");
        System.out.println("Susy - Susy likes to evenly space work throughout their week\nso that they don't feel overwhelmed\n");
        System.out.println("Adriana - Adriana likes to plow through their work on certain days\nof the week so that they are free the rest of the week\n");
        System.out.print("Susy or Adriana? ");
        Boolean workerTypeSet = false;
        while (!this.workerTypeSet) {
            String worker = keyboard.nextLine();
            if (worker.equals("Susy") || worker.equals("Adriana")) {
                this.workerTypeSet = true;
                if (worker.equals("Susy")) {
                    this.workerType = "Susy";
                    System.out.println("Ok I have you as a Susy!");
                }
                else {
                    this.workerType = "Adriana";
                    System.out.println("Ok I have you as an Adriana!");
                }
            }
            else {
                System.out.println("Sorry, you have to type 'Susy' or 'Adriana'");
            }
        }
        keyboard.close();
        System.out.println("Your preferences are set.");
    }
    
    public String getWorkerType() {
        return this.workerType;
    }
        
    /*
    //test
    public static void main(String[] args) {
        // todays date
        LocalDate today = LocalDate.now();
        //homeworks
        Task hw1 = new Task("physics worksheet", 2, 2., 2020, 5, 8);
        Task hw2 = new Task("finish ZhuLi", 3, 3., 2020, 5, 7);
        Task hw3 = new Task("finish physics test", 3, 3., 2020, 5, 8);
        Task hw4 = new Task("math oral exam", 4, 10., 2020, 5, 15);
        ZhuliUser Mario = new ZhuliUser();
        Mario.addTask(hw1);
        Mario.addTask(hw2);
        Mario.addTask(hw3);
        Mario.addTask(hw4);
        ArrayList<Task> tasksArray = Mario.getTasks();
        System.out.println("hw1 task array returned");
        String tasksString = tasksArray.toString();
        System.out.printf("tasks are: %s\n", tasksString);
        Double hrsLeft = Mario.getHoursLeftInWeek(today);
        System.out.printf("hours left in week: %f\n", hrsLeft);
        Mario.addTask(hw2);
        Mario.getTasks();
        Mario.getHoursLeftInWeek(today);
        Mario.addTask(hw3);
        Mario.getTasks();
        Mario.getHoursLeftInWeek(today);
        Mario.addTask(hw4);
        Mario.getTasks();
        Mario.getHoursLeftInWeek(today);
        Mario.setPreferences();
        Mario.getWorkerType();

    }
    */
}
        
    
                               
                
                
        
    
        
        
        
            