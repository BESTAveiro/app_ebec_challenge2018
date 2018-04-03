package com.example.jmfs1.ebec.schedulefragment.classes;

/**
 * Created by eu on 21/12/2016.
 */

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that defines an entire day for the schedule.
 */
public class DaySchedule implements Serializable {
    /**
     * Class that defines an event of the schedule.
     */
    public class Event implements Comparable<Event>, Serializable {
        /**
         * Starting hour of the event.
         */
        private String start_hour;
        /**
         * Finishing hour of the event.
         */
        private String finish_hour;
        /**
         * Name of the event.
         */
        private String name;
        /**
         * Description of the event.
         */
        private String description;

        /**
         * Constructor for this class.
         * @param start_hour The starting hour of the event.
         * @param finish_hour The finishing hour of the event.
         * @param name The name of the event.
         * @param description The description of the event.
         */
        public Event(String start_hour, String finish_hour, String name, String description) {
            this.start_hour = start_hour;
            this.finish_hour = finish_hour;
            this.name = name;
            this.description = description;
        }

        /**
         * Get method for the starting hour of the event.
         * @return The starting hour of the event.
         */
        public String getStartHour() {
            return start_hour;
        }

        /**
         * Get method for the finishing hour of the event.
         * @return The finishing hour of the event.
         */
        public String getFinishHour() {
            return finish_hour;
        }

        /**
         * Get method for the name of the event.
         * @return The name of the event.
         */
        public String getName() {
            return name;
        }

        /**
         * Get method for a description of the event.
         * @return The description of the event.
         */
        public String getDescription() {
            return description;
        }

        @Override
        public int compareTo(Event another) {
            return start_hour.compareTo(another.getStartHour());
        }
    }

    private int day;
    private ArrayList<Event> events;

    public DaySchedule(int day){
        this.day = day;
        this.events=new ArrayList<>();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public int getNumOfEvents() { return events.size(); }

    public Event getByIndex(int index) { return events.get(index); }

    public boolean insertEvent(String start_hour, String finish_hour, String name, String description){
        Event e = new Event(start_hour, finish_hour, name, description);

        return events.add(e);
    }

    public void sortEvents(){
        Collections.sort(events);
    }

}
