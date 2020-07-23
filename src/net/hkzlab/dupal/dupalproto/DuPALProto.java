package net.hkzlab.dupal.dupalproto;

import java.util.ArrayList;

public class DuPALProto {
    private final static String CMD_START = ">";
    private final static String CMD_END = "<";
   
    private final static String RESP_START = "[";
    private final static String RESP_END = "]";

    private final static char CMD_WRITE = 'W';
    private final static char CMD_READ = 'R';
    private final static char CMD_EXIT = 'X';
    private final static char CMD_RESET = 'K';

    private final static String CMD_RESP_ERROR = "CMD_ERROR";

    public static String buildREADCommand() {
        return CMD_START+CMD_READ+CMD_END;
    }

    public static String buildWRITECommand(int address) {
        return ""+CMD_START+CMD_WRITE+" "+String.format("%08X", address & 0x3FFFF)+CMD_END;
    }

    public static String buildEXITCommand() {
        return ""+CMD_START+CMD_EXIT+CMD_END;
    }

    public static String buildRESETCommand() {
        return ""+CMD_START+CMD_RESET+CMD_END;
    }

    public static String[] parseResponse(String response) {
        ArrayList<String> respString = new ArrayList<>();

        response = response.trim();

        if(response.equals(CMD_RESP_ERROR)) {
            respString.add(CMD_RESP_ERROR);
        } else if(response.startsWith(RESP_START) && response.endsWith(RESP_END)) {
            response = response.substring(1, response.length()-1).trim();
            char command = response.charAt(0);
            switch(command) {
                case CMD_READ: 
                case CMD_WRITE: {
                        String[] cmd_comp = response.split(" ");
                        if(cmd_comp.length != 2) return null;
                        return cmd_comp;
                    }
                default:
                    return null;
            }

        } else return null;

        return respString.toArray(new String[respString.size()]);
    }
}