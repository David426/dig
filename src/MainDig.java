import org.xbill.DNS.Record;

import java.util.Date;

public class MainDig {
    public static void main(String[] args) {
        if(args.length > 0){
            DigTimer digTimer = new DigTimer();
            digTimer.newQuery(args[0]);
            Record rec = digTimer.runDig();
            System.out.println("ANSWER SECTION: ");
            System.out.println(rec.toString());

            System.out.printf("\nQuery Time: %dmsec\n", (int)digTimer.getLastTime());
            System.out.println("WHEN: "+ new Date().toString());
        }
    }
}
