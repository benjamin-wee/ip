package duke;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * contains list of tasks in Duke
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * constructor for new TaskList instance from arraylist of tasks
     * 
     * @param tasks ArrayList of tasks to copy over
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * constructor for new TaskList instance
     */
    public TaskList() {
        this.tasks = new ArrayList<Task>();
    }

    /**
     * adds a new Todo into arraylist
     *
     * @param command input by the user
     *
     */
    public String addTodo(String command) {
        try {
            String description = command.replace("todo", "");
            Todo todo = new Todo(description);
            tasks.add(todo);
            return "Got it. I've added this task:\n" + todo.toString() + "\nNow you have "
                    + tasks.size() + " tasks in the list.";
        } catch (MissingDescriptionException e) {
            return e.toString();
        }
    }

    /**
     * adds a new Deadline into arraylist
     *
     * @param command input by the user
     *
     */
    public String addDeadline(String command) {
        try {
            String fullDescription = command.replace("deadline", "");
            String[] descriptionAndDate = fullDescription.split("/by ");
            String deadlineDescription = descriptionAndDate[0];
            String date = descriptionAndDate[1];
            LocalDate dateString = LocalDate.parse(date);
            Deadline deadline = new Deadline(deadlineDescription, dateString);
            tasks.add(deadline);
            return "Got it. I've added this task:\n" + deadline.toString() + "\nNow you have "
                    + tasks.size() + " tasks in the list.";
        } catch (MissingDescriptionException e) {
            return e.toString();
        } catch (DateTimeParseException e) {
            return "Please input date in YYYY-MM-DD format!";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Please fill in all details (task description and date)!";
        }
    }

    /**
     * adds a new Event into arraylist
     *
     * @param command input by the user
     *
     */
    public String addEvent(String command) {
        try {
            String description = command.replace("event", "");
            String[] input = description.split("/from");
            String eventDescription = input[0];
            String[] remainder = input[1].split("/to");
            String from = remainder[0];
            String to = remainder[1];
            Event event = new Event(eventDescription, from, to);
            tasks.add(event);
            return "Got it. I've added this task:\n" + event.toString() + "\nNow you have "
                    + tasks.size() + " tasks in the list.";

        } catch (MissingDescriptionException e) {
            return e.toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Please fill in all details (task description, start and end time)!";
        }
    }
    /**
     * marks a task in the arraylist as completed
     * 
     * @param command user input
     * @throws IndexOutOfBoundsException
     */
    public String markTask(String command) throws IndexOutOfBoundsException {
        try {
            String str = command.split(" ", 2)[1];
            int index = Integer.parseInt(str);
            tasks.get(index - 1).mark();
            return "Nice! I've marked this task as done:\n" + tasks.get(index - 1).toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Please give the index of the task you wish to mark!";
        } catch (IndexOutOfBoundsException e) {
            return "There are only " + tasks.size() + " tasks in the list!";
        }
    }

    /**
     * unmarks a task in arraylist to not done
     * 
     * @param command user input
     * @throws IndexOutOfBoundsException
     */
    public String unmarkTask(String command) throws IndexOutOfBoundsException {
        try {
            String str = command.split(" ", 2)[1];
            int index = Integer.parseInt(str);
            tasks.get(index - 1).unmark();
            return "OK, I've marked this task as not done yet:\n" + tasks.get(index - 1).toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Please give the index of the task you wish to unmark!";
        } catch (IndexOutOfBoundsException e) {
            return "There are only " + tasks.size() + " tasks in the list!";
        }
    }

    /**
     * removes a task from the arraylist
     * 
     * @param command input by user
     * @throws IndexOutOfBoundsException
     */
    public String deleteTask(String command) throws IndexOutOfBoundsException {
        try {
            String str = command.split(" ", 2)[1];
            int index = Integer.parseInt(str);
            Task task = tasks.remove(index - 1);
            return "Noted. I've removed this task:\n" + task.toString() + "\nNow you have "
                    + tasks.size() + " tasks in the list.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Please give the index of the task you wish to delete!";
        }
    }

    /**
     * finds tasks from the tasklist that contain description matching given keyword
     * 
     * @param command input by user
     */
    public String findTask(String command) throws DukeException {
        try {
            String keyword = command.split(" ", 2)[1];
            ArrayList<Task> matchingTasks = new ArrayList<Task>();
            for (Task task : tasks) {
                if (task.toString().contains(keyword)) {
                    matchingTasks.add(task);
                }
            }
            if (matchingTasks.size() == 0) {
                return "There are no tasks with the given keyword";
            }
            TaskList matches = new TaskList(matchingTasks);
            assert matches.getSize() != 0 : "Matching tasklist should not be empty";
            return "Here are the matching tasks in your list:\n" + matches.toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Please input the keyword!";
        }
    }

    /**
     * gets the size of the tasklist
     * 
     * @return an integer representing the size of the tasklist
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * returns the string representation of tasks in the tasklist to be saved into
     * the text file
     * 
     * @return string representation of tasks in the tasklist to be saved into the
     *         text file
     */
    public String toStorageData() {
        String data = "";
        for (Task task : tasks) {
            data += task.toStorageData();
            data += "\n";
        }
        return data.trim();
    }

    /**
     * returns the string representation of tasks in the tasklist with indexing
     * starting from 1
     * 
     * @return string representation of tasks in the tasklist with indexing starting
     *         from 1
     */
    @Override
    public String toString() {
        String taskToText = "";
        if (tasks.size() == 0) {
            return "There are no tasks as of now!";
        }
        for (int i = 1; i <= tasks.size(); i++) {
            Task task = tasks.get(i - 1);
            String line = Integer.toString(i) + ". " + task.toString();
            taskToText += line;
            taskToText += "\n";
        }
        assert !taskToText.isEmpty() : "Tasklist should not be empty";
        return taskToText.trim();
    }

}
