package com.zsh.task.swing;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("theme")
public class MenuItemController extends JMenuItem {
    private String className;
    private String themeName;
//    @Autowired
//    MyClient client;

    public MenuItemController (){
        super();
    }

    public MenuItemController(String className,String themeName){
        super();
        this.className = className;
        this.themeName = themeName;
        this.setText(themeName);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                MenuItemController c = (MenuItemController)e.getComponent();
                String c_name = c.getClassName();
                try {
                    UIManager.setLookAndFeel(c_name);
                    SwingUtilities.invokeLater(()-> updateAllWindows());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        this.addMouseListener(mouseAdapter);
    }
    /**
     * 更新所有顶层窗口的UI
     */
    private void updateAllWindows() {
        // 更新当前窗口
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window.isVisible() && window instanceof MyClient) {
                SwingUtilities.updateComponentTreeUI(window);

                // 如果是JFrame或JDialog，重新pack以适应新的布局
//                if (window instanceof JFrame) {
//                    ((JFrame) window).pack();
//                } else if (window instanceof JDialog) {
//                    ((JDialog) window).pack();
//                }
            }
        }
    }
    /**
     * 获取所有可用的主题
     * */
    public List<MenuItemController> getItems(){
        List<MenuItemController> re = new ArrayList<>();

        String sys = System.getProperty("os.name").toLowerCase();

        if(sys.contains("mac")){
            MenuItemController dark = new MenuItemController("com.formdev.flatlaf.themes.FlatMacDarkLaf", "Dark");
            MenuItemController light = new MenuItemController("com.formdev.flatlaf.themes.FlatMacLightLaf", "Light");

            re.add(dark);
            re.add(light);
        } else if (sys.contains("win")) {
            MenuItemController dark = new MenuItemController("com.formdev.flatlaf.FlatDarkLaf", "Dark");
            MenuItemController darcula = new MenuItemController("com.formdev.flatlaf.FlatDarculaLaf", "Darcula");
            MenuItemController intelliJ = new MenuItemController("com.formdev.flatlaf.FlatIntelliJLaf", "IntelliJ");
            MenuItemController light = new MenuItemController("com.formdev.flatlaf.FlatLightLaf", "Light");

            re.add(dark);
            re.add(light);
            re.add(darcula);
            re.add(intelliJ);
        }
        return re;
    }

    public String getClassName() {
        return className;
    }

    public String getThemeName() {
        return themeName;
    }
}
