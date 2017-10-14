package com.dbanalyzer;

import com.dbanalyzer.commands.RunnablePayload;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

@Component
public class Analyzer implements KeyListener{

    @Autowired private AnalyzerCommands analyserCommands;
    @Autowired private QuerySvcRemoteWrapper querySvcRemote;

    private JTextArea component;
    private JFrame frame;
    private JScrollPane scrollPane;
//    private StringBuilder builder;
    private List<String> history;
    private Integer pointer;

    public Analyzer() {
//        builder = new StringBuilder();
        history = new ArrayList<>();
        history.add("");
        pointer = 0;

        component = new JTextArea("Welcome\n> ");
//        "monospaced", Font.PLAIN
        component.setFont(new Font("monospaced",Font.BOLD,28));
        component.addKeyListener(this);
        component.setSize(5000, 1000);
        component.setLineWrap(true);

        scrollPane = new JScrollPane(component);
        frame = new JFrame();

        frame.add(scrollPane);
        frame.setSize(5000, 1000);
    }

    private void run(String[] args) {
        frame.setVisible(true);
    }

    private void handleCommand(String cmd) {
        component.append("Received '" + cmd + "' from user\n");

        RunnablePayload runnable = analyserCommands.get(cmd);
        if (runnable == null) {
            component.append("Command '" + cmd + "' is not recognized\n");
            component.append(analyserCommands.usage());
            return;
        }
        component.append(runnable.run(cmd));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Start Drone Server Analyzer");
        Analyzer analyzer = AppConfig.context.getBean(Analyzer.class);
        analyzer.run(args);
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        System.out.println(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String command = getLastLine();
            component.append("\n");
            System.err.println(command);
            if (command != null && !command.isEmpty()) {
                handleCommand(command);
                history.add(command);
                pointer = history.size() - 1;
            }
//            builder = new StringBuilder();
            e.consume();
            component.append("> ");
            component.setCaretPosition(component.getDocument().getLength());
        }
        else if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                handleHistory(e.getKeyCode());
                e.consume();
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                handleHistory(e.getKeyCode());
                e.consume();
            }
        }

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    private void handleHistory(int keyCode) {
        try {
            if (history.isEmpty())
                return;

            String content = component.getDocument().getText(0, component.getDocument().getLength() - 1);
            int lastLineBreak = content.lastIndexOf('\n');
            System.out.println(lastLineBreak + " " + component.getDocument().getLength());
            component.getDocument().remove(lastLineBreak, component.getDocument().getLength() - lastLineBreak);
            component.append("\n> ");

            if (keyCode == KeyEvent.VK_UP) {
                System.err.println("Up");
                component.append(history.get(pointer));
//                builder = new StringBuilder();
//                builder.append(history.get(pointer));
                if (pointer > 0)
                    pointer--;
            }

            if (keyCode == KeyEvent.VK_DOWN) {
                System.err.println("Down");
                component.append(history.get(pointer));
//                builder = new StringBuilder();
//                builder.append(history.get(pointer));
                if (pointer < history.size() - 1)
                    pointer++;
            }
        }
        catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println(e);
    }

    private String getLastLine() {
        try {
            String content = component.getDocument().getText(0, component.getDocument().getLength());
            int lastLineBreak = content.lastIndexOf('\n');
            System.out.println(lastLineBreak + " " + component.getDocument().getLength());
            String res = component.getDocument().getText(lastLineBreak + 1, component.getDocument().getLength() - 1 - lastLineBreak);
            if (res.startsWith("> "))
                return res.substring("> ".length());
            return res;
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
