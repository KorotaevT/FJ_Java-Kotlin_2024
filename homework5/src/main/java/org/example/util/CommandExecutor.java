package org.example.util;

import lombok.RequiredArgsConstructor;
import org.example.pattern.command.Command;

import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public class CommandExecutor {

    private final ExecutorService fixedThreadPool;

    public void executeCommand(Command command) {
        fixedThreadPool.submit(command::execute);
    }

}