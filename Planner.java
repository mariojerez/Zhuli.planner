import java.util.*;
import java.time.*;
    
public class Planner {
    // instance variables
    private Double hoursOfWorkDue;
    private Double hoursLeftThisWeek;
    private Hashtable<DayOfWeek, ArrayList<Task>> susyWeekPlan;
    private Hashtable<DayOfWeek, ArrayList<Task>> adrianaWeekPlan;
    public LocalDate today;
    public ZhuliUser user;
    public String workerType;
    public ArrayList<DayOfWeek> overBookedDays;
    public ArrayList<Task> tasksForTheWeek;
    
    
    // constructor
    public Planner (ZhuliUser user) {
        this.today = getTodaysDate();
        this.user = user;
        this.hoursOfWorkDue = getHoursDue();
        this.hoursLeftThisWeek = getHoursLeftThisWeek();
        this.workerType = user.getWorkerType();
        this.overBookedDays = new ArrayList<DayOfWeek>();
        this.tasksForTheWeek = new ArrayList<Task>();
        this.susyWeekPlan = new Hashtable<DayOfWeek, ArrayList<Task>>();
        this.adrianaWeekPlan = new Hashtable<DayOfWeek, ArrayList<Task>>();
        for (int i = 0; i < this.user.daysOfWeekArray.length; i++) {
            DayOfWeek weekday = this.user.daysOfWeekArray[i];
            ArrayList<Task> taskArray_S = new ArrayList<Task>();
            ArrayList<Task> taskArray_A = new ArrayList<Task>();
            this.susyWeekPlan.put(weekday, taskArray_S);
            this.adrianaWeekPlan.put(weekday, taskArray_A);
        }
    }
    
    public void update() {
        this.hoursOfWorkDue = getHoursDue();
        this.hoursLeftThisWeek = getHoursLeftThisWeek();
        this.workerType = user.getWorkerType();
        this.today = getTodaysDate();
    }
    
    public String getTasksForTheWeek() {
        String tasksForWeek = this.tasksForTheWeek.toString();
        return tasksForWeek;
    }
    
    public void setTasksForWeek() {
        Boolean canFinishThisWeek = checkIfCanFinishThisWeek();
        if (canFinishThisWeek) {
            this.tasksForTheWeek = (ArrayList<Task>) user.tasks.getOrderedArray();
        }
        else {
            ArrayList<Task> allOrderedTasks = user.tasks.getOrderedArray();
            fitTasksIntoWeek(allOrderedTasks);
        }
    }

    
    public void setSchedule() {
        setTasksForWeek();
        if (this.user.getWorkerType().equals("Susy")) {
            fillSusysWeek();
        }
        else if (this.user.getWorkerType().equals("Adriana")) {
            fillAdrianasWeek();
        }
    }
    
    public void fillSusysWeek() {
        Task lastTask = null;
        Double overBookedHrs = 0.;
        
        while (!this.tasksForTheWeek.isEmpty()) {
            Boolean active = false;
            for (DayOfWeek weekday : this.susyWeekPlan.keySet()) {
                if (!active) {
                    if (weekday == this.today.getDayOfWeek()) {
                        active = true;
                    }
                }
                if (active) {
                    Double hrsLeftInDay = getHoursLeftInDay(weekday);
                    if (hrsLeftInDay > 0) {
                        if (this.tasksForTheWeek.size() > 0) {
                            fitTask(this.tasksForTheWeek.get(0), weekday, hrsLeftInDay);//write
                        }
                    }
                    
                }
            }
        }
    }
    public Double getHoursLeftInDay(DayOfWeek weekday) {
        Double maxDayWorkHours = this.user.getMaxWorkHours(weekday);
        Double hrsBookedThisDay = 0.;
        if (this.user.getWorkerType().equals("Susy")) {
            for (Task task : this.susyWeekPlan.get(weekday)) {
                hrsBookedThisDay += task.getHoursRequired();
            }
        }
        else if (this.user.getWorkerType().equals("Adriana")) {
            ArrayList<Task> weekTaskArray = this.adrianaWeekPlan.get(weekday);
            for (Task task : weekTaskArray) {
                hrsBookedThisDay += task.getHoursRequired();
            }
        }
        Double hrsLeft = maxDayWorkHours - hrsBookedThisDay;
        return hrsLeft;
    }
    
    public void fitTask(Task task,DayOfWeek weekday,Double hrsLeftInDay) { // used by Susy type worker
            ArrayList<Task> susysWeekdayTasks = susyWeekPlan.get(weekday);
            susysWeekdayTasks.add(task);
            if (task.getHoursRequired() > hrsLeftInDay) {
                Double hoursWorked = hrsLeftInDay;
                task.updateProgressByHour(hoursWorked);
            }
            else {
                this.tasksForTheWeek.remove(task);
            }
        }
            
            
        
            
    public void fillAdrianasWeek() {
        Boolean active = false; // when true we are at current days
        Task lastTask = null; // from the day before
        Double overBookedHrs = 0.; // hrs left from last task yesterday
        // for every week day in adriana's week's plan
        for (DayOfWeek day : this.adrianaWeekPlan.keySet()) {
            if (!active) {
                // if the weekday is the same as todays weekday, lets begin because we are current
                if (day == this.today.getDayOfWeek()) {
                    active = true;
                }
            }
            // if we are current
            if (active) {
                // available hrs for the day
                Double availableHours = this.user.day_hoursHash.get(day);
                Double hoursFilled = 0.;
                // array of tasks for the day
                ArrayList<Task> taskArray = new ArrayList<Task>();
                //while there is still time left int the day
                while (hoursFilled < availableHours) {
                    // if there is a task that wasn't finished yesterday
                    if (overBookedHrs != 0) {
                        taskArray.add(lastTask);
                        hoursFilled += overBookedHrs;
                        overBookedHrs = 0.;
                    }
                    // if the arraylist tasksfortheweek is not empty:
                    else if (!this.tasksForTheWeek.isEmpty()) {
                        Task task = this.tasksForTheWeek.get(0); // get first task in list
                        taskArray.add(task);
                        this.tasksForTheWeek.remove(task);
                        hoursFilled += task.getHoursRequired();
                        lastTask = task;
                    }
                    // if there are no more tasks for the week, exit the whileloop
                    else if (this.tasksForTheWeek.isEmpty()) {
                        break;
                    }
                }
                if (hoursFilled > availableHours) {
                    Double overbookedHours = hoursFilled - availableHours;
                }
                this.adrianaWeekPlan.put(day, taskArray);
            }
            if (overBookedHrs > 0 && this.overBookedDays.contains(day)) {
                this.overBookedDays.add(day);
            }
            else if (overBookedHrs == 0 && this.overBookedDays.contains(day)) {
                this.overBookedDays.remove(day);
            }
        }
    }
        
           
        
    public Boolean checkIfCanFinishThisWeek() {
        if (getHoursDue() <= getHoursLeftThisWeek()) {
            return true;
        }
        return false;
    }
    
    public Double getHoursDue() { // total hours of work to do
        Double totalHours = 0.;
        ArrayList<Task> taskArray = this.user.getTasks();
        for (Task task : taskArray) {
            Double hrsForTask = task.getHoursRequired();
            totalHours += hrsForTask;
        }
        return totalHours;
    }
    // a week ends on Saturday
    public Double getHoursLeftThisWeek() {
         return this.user.getHoursLeftInWeek(this.today);
    }
    
    public LocalDate getTodaysDate() {
        LocalDate today = LocalDate.now();
        return today;
    }
    
    public void fitTasksIntoWeek(ArrayList<Task> allOrderedTasks) {
        Double hrsLeftInWeek = this.user.getHoursLeftInWeek(this.today);
        Boolean weekFull = false;
        while (!weekFull) {
            for (Task task : allOrderedTasks) {
                this.tasksForTheWeek.add(task);
                hrsLeftInWeek -= task.getHoursRequired();
                if (hrsLeftInWeek == 0) {
                    weekFull = true;
                }
                else if (hrsLeftInWeek < 0) {
                    this.tasksForTheWeek.remove(task);
                    hrsLeftInWeek += task.getHoursRequired();
                    weekFull = true;
                }
            }
        }
    }
    
    public ArrayList<Task> toDoToday() {
        LocalDate today = getTodaysDate();
        DayOfWeek weekday = today.getDayOfWeek();
        ArrayList<Task> todaysTasks;
        if (this.user.getWorkerType().equals("Susy")) {
            return this.susyWeekPlan.get(weekday);
        }
        else {
            return this.adrianaWeekPlan.get(weekday);
        }
    }
                
    public static void main(String[] args) {
        Task hw1 = new Task("homework 1", 2, 2., 2020, 6, 8);
        Task hw2 = new Task("homework 2", 3, 3., 2020, 5, 21);
        Task hw3 = new Task("homework 3", 3, 3., 2020, 5, 19);
        Task hw4 = new Task("homework 4", 4, 4., 2020, 5, 15);
        Task hw5 = new Task("homework 5", 2, 2., 2020, 5, 20);
        ZhuliUser Mario = new ZhuliUser();
        Mario.addTask(hw1);
        Mario.addTask(hw2);
        Mario.addTask(hw3);
        Mario.addTask(hw4);
        Mario.addTask(hw5);
        Planner MariosPlanner = new Planner(Mario);
        MariosPlanner.update();
        MariosPlanner.setSchedule();
        ArrayList<Task> listOfThingsToDoToday = MariosPlanner.toDoToday();
        for (Task task : listOfThingsToDoToday) {
            System.out.println(task);
        }
        // issue: seems to prioritize everything well except for the first hw added.
        // hw1 should not have been highest priority.
        
    }
            
}
            
        