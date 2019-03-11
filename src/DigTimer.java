import org.xbill.DNS.Record;

public class DigTimer {

    long start;
    long stop;
    long runTime;

    MyDig digger;
    public DigTimer(String inquery){
        digger = new MyDig(inquery);
        runTime = -1;
    }

    public Record runDig(){
        start = System.currentTimeMillis();
        Record rec = digger.run();
        stop = System.currentTimeMillis();
        runTime = stop - start;
        return rec;
    }

    public long getLastTime(){
        return runTime;
    }


}
