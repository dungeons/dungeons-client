package com.kingx.dungeons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Param {

    private String trigger;
    private final List<String> args = new ArrayList<String>();

    private Param(String trigger) {
        this.trigger = trigger;
    }

    public void add(String arg) {
        args.add(arg);
    }

    public String getTrigger() {
        return trigger;
    }

    public String getArgument(int i) {
        return args.get(i);
    }

    public static Map<String, Param> getParams(String[] args) {
        Map<String, Param> map = new HashMap<String, Param>();
        Param currentParam = null;
        for (String arg : args) {
            if (arg.startsWith("-")) {
                addParam(map, currentParam);
                currentParam = new Param(arg);
            } else {
                currentParam.add(arg);
            }
        }
        addParam(map, currentParam);
        return map;
    }

    private static void addParam(Map<String, Param> map, Param param) {
        if (param != null) {
            map.put(param.getTrigger(), param);
        }
    }

    @Override
    public String toString() {
        return "Param [trigger=" + trigger + ", args=" + Arrays.toString(args.toArray()) + "]";
    }

}
