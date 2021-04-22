package fun.jexing.connector;


public class RequestStringParser {
    private StringBuilder sb;
    private static String end = "\r\n";
    private String method;
    private String url;
    private String protocol;
    public RequestStringParser(StringBuilder sb){
        this.sb = sb;
        parse();
    }
    public void requestLine(){
        int endIndex = sb.indexOf(end);
        byte switchNum = 0;
        int left = 0;
        for (int i = 0; i <= endIndex; i++) {
            char c = sb.charAt(i);
            if (c == ' ' || c == '\r'){
                String item = sb.substring(left,i);
                switch (switchNum){
                    case 0:
                        method = item;
                        break;
                    case 1:
                        url = item;
                        break;
                    case 2:
                        protocol = item;
                        break;
                }
                switchNum++;
                left = i + 1;
            }
        }
    }
    public void parse(){
        requestLine();

    }
}
