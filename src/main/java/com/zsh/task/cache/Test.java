package com.zsh.task.cache;

import com.zsh.task.entity.User;

import javax.swing.*;
import java.awt.*;

public class Test {

    public static void main(String[] args) throws InterruptedException {
//        int [] arr1 = new int[]{1,2,3,0,0,0};
//        int [] arr2 = new int[]{2,5,6};
//        mergeArray(arr1,3,arr2,3);
//        System.out.println(Arrays.toString(arr1));
        // 创建一个叫无名窗口
        JFrame window = new JFrame();
        // 设置窗口在屏幕上的位置和大小
        window.setBounds(0, 0, 1086, 800);
        // 设置窗口可见
        // 注意：确保在所有组件添加完毕后再设置窗口可见
        // 设置窗口标题
        window.setTitle("测试");

        // 创建菜单栏
        JMenuBar mb = new JMenuBar();
        // 创建File菜单
        JMenu file = new JMenu("File");

        // 创建具体的菜单项
        JMenuItem openItem = new JMenuItem("Open");
        // 为Open菜单项添加动作监听器
        openItem.addActionListener(e -> SwingUtilities.invokeLater(() -> new JFileChooser().showOpenDialog(window)));
        // 将Open菜单项添加到File菜单
        file.add(openItem);

        // 创建Help菜单
        JMenu help = new JMenu("Help");
        // 将File和Help菜单添加到菜单栏
        mb.add(file);
        mb.add(help);
        // 设置窗口的菜单栏
        window.setJMenuBar(mb);
        // 设置窗口图标
        window.setIconImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/xiaoheizi_icon.jpg"));

        // 设置窗口关闭操作
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口可见
        window.setVisible(true);

    }
    /**
     * 输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
     *      * 输出：[1,2,2,3,5,6]
     * */

    public static void mergeArray(int [] arr1 ,int m,int [] arr2,int n){
        int[] temp = new int[m + n];
        int point1 = 0, point2 = 0, point3 = 0;

        // 合并两个数组直到其中一个数组耗尽
        while (point1 < m && point2 < n) {
            if (arr1[point1] == 0) {
                temp[point3++] = arr2[point2++];
            } else if (arr2[point2] == 0) {
                temp[point3++] = arr1[point1++];
            } else if (arr2[point2] > arr1[point1]) {
                temp[point3++] = arr1[point1++];
            } else {
                temp[point3++] = arr2[point2++];
            }
        }

        // 如果 arr1 还有剩余元素，复制到 temp
        while (point1 < m) {
            temp[point3++] = arr1[point1++];
        }

        // 如果 arr2 还有剩余元素，复制到 temp
        while (point2 < n) {
            temp[point3++] = arr2[point2++];
        }

        System.arraycopy(temp,0,arr1,0,temp.length);
    }
}
