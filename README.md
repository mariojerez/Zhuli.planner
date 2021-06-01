# Zhuli.planner
Zhuli Moon Planner: Your Personal Planning Assistant
By Mario Jerez
Data Structures
08 May 2020

The name of the program was inspired by a character from The Legend Of Kora named Zhuli Moon. She is the assistant of a rich business man and inventor. In the show, no matter how vague and confusing his requests/expectations of her are, she always knows exactly what he needs and provides it expertly. She does all the dirty work so that he only has to worry about thinking and inventing.

I wrote the Zhuli program because I was exhausted of having to think about a schedule for getting all of my schoolwork done. I felt I would be way more productive if I just had someone I trusted and who knew my schedule who would tell me what I have to do to stay on track. And because I’m not good at planning out my work schedule so that I am on track to meet deadlines, I am constantly thinking of other work I have to do even while I’m working. This kind of stress is just unproductive.

Therefore, I made my program able to take in tasks and some information and just tell me what I have to do today/this week to stay on track. That way I don’t have to waste energy on thinking about deadlines. When making your ZhuliUser, you can select one out of two worker types, depending on how you like to space work across your week. I used the Heap class we wrote for class (and added one extra function at the end) that organizes the tasks by priority level. Higher priority values get sorted closer to the top so that they get done first. The priority values of tasks are determined by the days until deadline, its initial importance value assigned, along with some other things. The highest priorities are closest to 2 and the lowest priorities are closest to 1. This was achieved by using an infinite series that converges at 2.

How to use Zhuli:

You should have four classes open: Heap.java, Planner.java, Task.java, and ZhuliUser.java. The plan was to write a Zhuli.java class that would make the graphical interface, but I didn’t have time. If you’d like, you can just run Planner.java because I wrote a test method that creates a user called Mario, five homework assignments, and a planner called MariosPlanner. The most important thing to see is the setSchedule Planner function. If the worker type associated to the user is “Susy”, then it will try to spread few tasks throughout the week. If your worker type is “Adriana”, it will try to pile assignments into the next few days of the week. In the Planner class, the first day of the week is Sunday and the last day of the week is Saturday. If you enact the setSchedule() function on Saturday, it will only assign tasks for Saturday.

There are a lot of improvements to made. For one, an issue that the program has right now is that when you add a user to planner and add tasks to the user, it will correctly sort the tasks by priority except for the first task added. For some reason, it will always want you to do the first task added first. I also haven’t extensively tested the program for scenarios where the hours required to complete all of the tasks exceeds the number of work hours in the user’s week (which can be customized with the preferences function). I also wanted the Planner to be able to check if it is possible to complete all of the tasks by the deadline given the workhour preferences but I haven’t gotten to that.

Overall, I am very excited about my Zhuli program. Right now I have a pretty rough draft of the program completed. Over the summer, I want to keep working on it and keep adding features to it. I’m even thinking of learning how to put the program on a website or app so that people can use it for themselves! I think it’s a pretty cool business idea and is a good opportunity for project-oriented learning.
Some functions to play with:
Construct a task: Task taskName = new Task( String title, int importance, Double hrsRequired)
(You may notice that there are two constructors. One was made for faster testing);
Construct a new User: ZhuliUser newUser = new ZhuliUser()
Add a task to user: newUser.addTask(Task taskName)
Set user preferences: newUser.setPreferences()
Construct new planner: Planner usersPlanner = new Planner(ZhuliUser user)
(This automatically gives the planner access to the user’s tasks).
Catalogue tasks into certain day of the week: usersPlanner.setSchedule()
Get what you have to do today: usersPlanner.toDoToday()

