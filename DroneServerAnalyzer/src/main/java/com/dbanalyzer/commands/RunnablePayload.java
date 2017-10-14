package com.dbanalyzer.commands;

import com.generic_tools.Pair.Pair;

import java.util.List;

public interface RunnablePayload {

    boolean isRelevant(String payload);

    List<Pair<String, String>> getUsage();

    String run(String payload);
}
