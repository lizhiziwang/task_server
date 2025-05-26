package com.zsh.task.swing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


@Slf4j
@Component("FILE")
public class FileMenuItemController extends JMenuItem {
    public FileMenuItemController(){
        super();
    }

    public static void main(String[] args) {
//        JFrame frame = new JFrame("打开文件夹示例");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(300, 200);
//
//        JButton openButton = new JButton("打开文件夹");
//        openButton.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//            int result = fileChooser.showOpenDialog(frame);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                String selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
//                System.out.println("Selected path: " + selectedPath);
//            }
//        });
//
//        frame.add(openButton);
//        frame.setVisible(true);

        String path = "D:\\data\\番禺职院\\shp_re/line.shp";
        File f = new File(path);

        System.out.println(f.getName());
    }

}
