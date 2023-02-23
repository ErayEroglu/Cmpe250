import java.util.Comparator;

public class FlightComparator implements Comparator<Flight> { // check 1 and -1 values. Be sure queue is in order

    boolean isACC;

    public FlightComparator(boolean isACC) {
        this.isACC = isACC;
    }

    public int compare(Flight f1, Flight f2) {

        if (f1.getMoment() < f2.getMoment()) {  // first look at when they entried the queue
            return -1;
        } else if (f1.getMoment() > f2.getMoment()) {
            return 1;
        }

        if (isACC) {
            if ((f1.getAge() && !f2.getAge())) {  // this condition is only valid for acc
                return -1;
            } else if ((!f1.getAge() && f2.getAge())) { 
                return 1;
            }
        }

        if (f1.getName().compareTo(f2.getName()) < 0) {  // if the preconditions are same, do string comparison
            return -1;
        } else {
            return 1;
        }

    }

}