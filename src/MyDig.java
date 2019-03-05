/*David Sill
CSE 310 Assignment: 2
*/
import org.xbill.DNS.*;

import java.io.IOException;
import java.net.UnknownHostException;


public class MyDig {

    final String[] ROOT_SERVERS = {"198.41.0.4", "199.9.14.201", "192.33.4.12", "199.7.91.13", "192.203.230.10", "192.5.5.241",
    "192.112.36.4", "198.97.190.53", "192.36.148.17", "192.58.128.30", "193.0.14.129", "199.7.83.42", "202.12.27.33"};

    String inquery;

    public MyDig(String inquery){
        this.inquery = inquery;
    }

    public Record run(){
        return executeQuery(inquery, ROOT_SERVERS[0], ROOT_SERVERS[0]);
    }

    public Record executeQuery(String queryAddress, String host, String currentAuth){
        System.out.printf("Query: [%s]->[%s] Auth:[%s]\n", queryAddress, host, currentAuth);
        Message query = createQuery(queryAddress);
        Message reply = null;
        try {
            SimpleResolver resolver= new SimpleResolver(host);
            reply = resolver.send(query);
        } catch (UnknownHostException e) {
            System.err.printf("Host [%s] was unable to be found\n", queryAddress);
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        if(reply == null){
            System.err.printf("Reply not found for [%s], [%s], [%s]\n", queryAddress, host, currentAuth);
            System.exit(-1);
        }
        Record[] answers = reply.getSectionArray(Section.ANSWER);
        Record[] authorityRecords = reply.getSectionArray(Section.AUTHORITY);
        Record[] additionalRecords = reply.getSectionArray(Section.ADDITIONAL);
        if(answers.length > 0){
            for (Record answer: answers) {
                if(answer.getType() == Type.CNAME){
                    queryAddress = ((CNAMERecord)answers[0]).getTarget().toString();
                    return executeQuery(queryAddress, currentAuth, currentAuth);
                } else {
                    return answer;
                }
            }
        } else if (additionalRecords.length > 0){
            for(Record adRecord : additionalRecords){
                if(adRecord.getType() == Type.A){
                    host = ((ARecord) adRecord).getAddress().getHostAddress();
                    return executeQuery(queryAddress, host, currentAuth);
                }
            }
        } else if (authorityRecords.length > 0){
            for(Record auRecord : authorityRecords){
                if(auRecord.getType() == Type.SOA){
                    queryAddress = ((SOARecord)auRecord).getAdmin().toString();
                    return executeQuery(queryAddress, currentAuth, currentAuth);
                } else if(auRecord.getType() == Type.NS){
                    System.out.println("here");;
                }
            }
            
        } else {
            System.err.printf("Empty reply  found for [%s], [%s], [%s]\n", queryAddress, host, currentAuth);
            System.exit(-1);
        }
        return null;
    }

    public static Message createQuery(String query){
        Name queryName = null;
        try {
            if (query.charAt(query.length()-1) != '.'){
                queryName = Name.concatenate(Name.fromString(query), Name.root);
            } else {
                queryName = Name.fromString(query);
            }
        } catch (TextParseException textEx){
            System.err.printf("Text Parse Exception of [%s]\n", query);
        } catch (NameTooLongException nameEx){
            System.err.printf("Name Too Long Exception of [%s]\n", query);
        }
//        System.out.println(queryName.toString());
        if(queryName != null){
            Record queryRecord = Record.newRecord(queryName, Type.A, DClass.IN);
            Message queryMessage = Message.newQuery(queryRecord);
            return queryMessage;
        } else {
            return null;
        }
    }
}

class DemoDigADome {
    public static void main(String[] args) {
        MyDig diggyboi = new MyDig("www.news12.com");
        Record answer = diggyboi.run();
        System.out.println(answer);
    }
}