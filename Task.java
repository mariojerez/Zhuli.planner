import java.util.*;
import java.time.*;

public class Task implements Comparable<Task> {

    // instance variables
    public String title;
    public LocalDate deadline;
    public Integer daysTilDeadline;
    public Double priority; // 1 lowest, 2 highest
    public int nSeries;
    public Double progressPercentage;
    public LocalDate dateAdded;
    public int initialImportance;
    public Double hoursRequired;
    public int id;
    public static int nextID = 1001;
    public Boolean completed;
    
    // constructor for faster testing
    public Task(String title, int importance, Double hrsRequired, int year, int month, int day) { // importance value 1-4 (2 is normal 4 is almost life or death)
        
        this.dateAdded = LocalDate.now();
        this.deadline = LocalDate.of(year, month, day);
        
        this.hoursRequired = hrsRequired;
        this.completed = false;
        this.title = title;
        this.progressPercentage = .0;
        this.initialImportance = importance;
        this.daysTilDeadline = getDaysTilDeadline();
        this.priority = 1.; // automatically lowest priority
        this.nSeries = 1;
        
        this.id = Task.nextID;
        Task.nextID++;
        updatePriority();
    }
    

    // constructor
    public Task(String title, int importance, Double hrsRequired) { // importance value 1-4 (2 is normal 4 is almost life or death)
        Scanner keyboard = new Scanner(System.in);
        System.out.printf("What year is %s due? (####) ", title);
        String yearStr = keyboard.nextLine();
        System.out.printf("What month is %s due? (##) ", title);
        String monthStr = keyboard.nextLine();
        System.out.printf("What day is %s due? (##) ", title);
        String dayStr = keyboard.nextLine();
        System.out.println("What hour (24 hr clock)? (##) ");
        String hourStr = keyboard.nextLine();
        System.out.println("Minutes? (##) ");
        String minStr = keyboard.nextLine();
        
        
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        int day = Integer.parseInt(dayStr);
        
        this.dateAdded = LocalDate.now();
        this.deadline = LocalDate.of(year, month, day);
        
        this.hoursRequired = hrsRequired;
        this.completed = false;
        this.title = title;
        this.progressPercentage = 0.;
        this.initialImportance = importance;
        this.daysTilDeadline = getDaysTilDeadline();
        this.priority = 1.; // automatically lowest priority
        this.nSeries = 1;
        
        this.id = Task.nextID;
        Task.nextID++;
        updatePriority();
    }
    
    public int compareTo(Task other) {
        if (this.priority < other.priority) return +1; // beacause heap places smallest objects
        if (this.priority > other.priority) return -1; // at the top
        return 0;
    }
    
    public String toString() {
        return this.title;
    }
    
    public void updateProgressByHour(Double hoursWorked) {
        Double prevPercentageLeft = 100 - this.progressPercentage;
        Double hoursLeft = this.hoursRequired - hoursWorked;
        Double percentLeft = prevPercentageLeft * hoursLeft / this.hoursRequired;
        this.progressPercentage = 100 - percentLeft;
        this.hoursRequired = hoursLeft;
    }

        
        // update progress percentage from last progress percentage
    
    public Integer getDaysTilDeadline() {
      int days = 1;
      LocalDate todaysDate = LocalDate.now();
        if (deadline.equals(todaysDate)) {
          //task is due today
          return 0;
        }
        if (deadline.isBefore(todaysDate)) {
          // task is late
          days = -1;
        }
        Period timeTilDeadline = Period.between(todaysDate, deadline);
        days = days * timeTilDeadline.getDays(); // mult. determines if late or not. -# is late.
        return days;
    }
    
    private Boolean taskCompleted() {
      Double percentLeft = 100. - this.progressPercentage;
      if (percentLeft == 0) {
        return true;
      }
      return false;
    }
      
    
    public void updatePriority() {
      this.completed = taskCompleted();
      if (this.completed) {
        this.priority = 0.;
        return;
      }
      // the 10000 is to keep the number of steps from going crazy. Will probably remove it or come up with a better solution.
      Double additionalSteps = ( (getHoursRequired() + 0.0001) * this.initialImportance) / ( 10000 * (getDaysTilDeadline() + .0001) * (this.progressPercentage + 0.0001)); // just in case zero
      this.priority += addSeries((int) Math.round(additionalSteps));
    }
      
    // this program simulates an infinite series that begins at 1 and converges at 2
    // to assign its priority value
    public Double addSeries(int additionalSteps) {
      Double value = 0.;
      for (int i = 0; i < additionalSteps; i++) {
        value += 1.0/Math.pow(2,nSeries); // 2^nSeries
        this.nSeries++;
      }
      return value;
    }
    
    public Double getHoursRequired() {
        return this.hoursRequired;
    }

//test
    public static void main(String[] args) {
        Task hw1 = new Task("physics worksheet", 2, 2., 2020, 5, 8);
        Task hw2 = new Task("finish ZhuLi", 3, 3., 2020, 5, 7);
        Task hw3 = new Task("finish physics test", 3, 3., 2020, 5, 8);
        Task hw4 = new Task("math oral exam", 4, 10., 2020, 5, 15);
        System.out.println(hw1);
        System.out.println(hw2);
        System.out.println(hw3);
        System.out.println(hw4);
        System.out.printf("homework1 days until deadline: %d days\n", hw1.getDaysTilDeadline());
        System.out.printf("homework2 days until deadline: %d days\n", hw2.getDaysTilDeadline());
        System.out.printf("homework3 days until deadline: %d days\n", hw3.getDaysTilDeadline());
        System.out.printf("homework4 days until deadline: %d days\n", hw4.getDaysTilDeadline());
        
        System.out.printf("priority of hw1: %.150f\n", hw1.priority);
        System.out.printf("priority of hw2: %.150f\n", hw2.priority);
        System.out.printf("priority of hw3: %.150f\n", hw3.priority);
        System.out.printf("priority of hw4: %.150f\n", hw4.priority);
        System.out.printf("compare hw1 to hw2: %d\n", hw1.compareTo(hw2));
        System.out.printf("compare hw3 to hw4: %d\n", hw3.compareTo(hw4));
        System.out.printf("compare hw1 to hw4: %d\n", hw1.compareTo(hw4));
        
        System.out.printf("hours required for hw1: %f\n", hw1.getHoursRequired());
        System.out.printf("hours required for hw2: %f\n", hw2.getHoursRequired());
        System.out.printf("hours required for hw3: %f\n", hw3.getHoursRequired());
        System.out.printf("hours required for hw4: %f\n", hw4.getHoursRequired());
    }
}

