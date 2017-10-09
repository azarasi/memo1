import java.awt.Font;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.stream.IIOByteBuffer;
import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Hashtable;

public class memo1 extends JFrame{
    private String lafClassName = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    private JFrame frm;
    private JTextArea tx;
    private JTree tree;
    private Hashtable<String, Integer> treedata;
    private String[] strs;
    private int line_dat[][];
    private static int win_width = 360, win_height = 640;

    public static void main(String[] args) {
        memo1 chou = new memo1();
        chou.init();
    }

    protected void init(){
        try {
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(frm);
        }catch (Exception a){
        }

        frm=new JFrame("メモ");
        frm.setBounds(10,10,win_width,win_height);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        set_menu();   //メニューバーの初期化
        tree_init();  //tree窓の初期化
        tx_init();    //本文窓の初期化

        frm.setVisible(true);
    }
    protected void set_menu() {
        Font font = new Font("Migu 1C",Font.BOLD,12);
        JMenuBar menubar= new JMenuBar();

        JMenu menu1 = new JMenu("File"); menubar.add(menu1);
        menu1.setFont(font);
        JMenuItem menuitem1 = new JMenuItem("New") ; menu1.add(menuitem1);
        JMenuItem menuitem2 = new JMenuItem("Open"); menu1.add(menuitem2);
        JMenuItem menuitem3 = new JMenuItem("Save"); menu1.add(menuitem3);
        menuitem3.setEnabled(false);

        JMenu menu2 = new JMenu("Edit"); menubar.add(menu2);
        menu2.setFont(font);
        menu2.setEnabled(false);

        menubar.add(Box.createHorizontalGlue());
        JMenu menu3 = new JMenu("Help"); menubar.add(menu3);
        menu3.setFont(font);
        JMenuItem menuitem4 = new JMenuItem("Ver") ; menu3.add(menuitem4);

        menuitem1.addActionListener(new ActionAdapter());
        menuitem2.addActionListener(new ActionAdapter());
        menuitem3.addActionListener(new ActionAdapter());
        menuitem4.addActionListener(new ActionAdapter());

        frm.setJMenuBar(menubar);//メニューバーをセットする。
    }
    protected void tree_init() {
        DefaultMutableTreeNode root0 = new DefaultMutableTreeNode("JavaDrive");
        DefaultMutableTreeNode root = tree_gen(root0);
        tree = new JTree(root);
        tree.setFont(new Font("Migu 1M",Font.PLAIN,12));
        tree.setRootVisible(true);

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.getViewport().setView(tree);
        scrollPane1.setPreferredSize(new Dimension(win_width*1/3,win_height-71));
        JPanel p = new JPanel();
        p.add(scrollPane1);
        frm.getContentPane().add(p, BorderLayout.WEST);
    }
    protected void tx_init() {
        tx=new JTextArea("パピプペポ abcdefg ABCDEFG これがテキストエリアです。");
        tx.setFont(new Font("Migu 1M",Font.PLAIN,12));
        JScrollPane scrollpane2 = new JScrollPane();
        scrollpane2.setViewportView(tx);
        frm.getContentPane().add(scrollpane2, BorderLayout.CENTER);
    }
    protected DefaultMutableTreeNode tree_gen(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode swing = new DefaultMutableTreeNode("Swing");
        DefaultMutableTreeNode java2d = new DefaultMutableTreeNode("Java2D");
        DefaultMutableTreeNode java3d = new DefaultMutableTreeNode("Java3D");
        DefaultMutableTreeNode javamail = new DefaultMutableTreeNode("JavaMail");

        DefaultMutableTreeNode swingSub1 = new DefaultMutableTreeNode("JLabel");
        DefaultMutableTreeNode swingSub2 = new DefaultMutableTreeNode("JButton");
        DefaultMutableTreeNode swingSub3 = new DefaultMutableTreeNode("JTextField");

        root.add(swing);
        swing.add(swingSub1);
        swing.add(swingSub2);
        swing.add(swingSub3);

        root.add(java2d);
        root.add(java3d);
        root.add(javamail);

        return(root);
    }

    class ActionAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("New")) {
                tx.setText("これが新規");
            }
            if (e.getActionCommand().equals("Open")) {

                JFileChooser filechooser = new JFileChooser("d:\\java_work\\intelliJ\\memo1\\src");
                String directory="";

                int selected = filechooser.showOpenDialog(frm);
                if (selected == JFileChooser.APPROVE_OPTION){
                    File file = filechooser.getSelectedFile();
                    directory = file.getAbsolutePath();
                    frm.setTitle(directory);
                }else if (selected == JFileChooser.CANCEL_OPTION){
                }else if (selected == JFileChooser.ERROR_OPTION){
                }
                
                try {
// 指定のファイル URL のファイルをバイト列として読み込む
                    byte[] fileContentBytes = Files.readAllBytes(Paths.get(directory));
// 読み込んだバイト列を UTF-8 でデコードして文字列にする
                    String fileContentStr = new String(fileContentBytes, StandardCharsets.UTF_8);
// レコード単位で分割
                    strs = fileContentStr.split("\r\n\\.");

                    treedata = new Hashtable<String, Integer>();
                    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Wzmemo");
                    DefaultMutableTreeNode AA = new DefaultMutableTreeNode();
                    DefaultMutableTreeNode BB = new DefaultMutableTreeNode();
                    DefaultMutableTreeNode CC = new DefaultMutableTreeNode();
                    int all_lines = strs.length;
                    String AAd,BBd,CCd;
                    for (int i = 0; i < all_lines; i++) {
                        if (strs[i].length() > 0) {
                            if (i==0) {
                                strs[i] = strs[i] + "\r\n";
                            }else {
                                strs[i] = '.' + strs[i] + "\r\n";
                            }
                            if ((strs[i].charAt(0) == '.') && (strs[i].charAt(1) != '.')) {
                                AAd =strs[i].substring(1,strs[i].indexOf("\r\n")-14) ;
                                AA = new DefaultMutableTreeNode(AAd);
                                root.add(AA);
                                treedata.put(AAd, i);
                            } else if ((strs[i].charAt(0) == '.') && (strs[i].charAt(1) == '.') && (strs[i].charAt(2) != '.')) {
                                BBd =strs[i].substring(2,strs[i].indexOf("\r\n")-14) ;
                                BB = new DefaultMutableTreeNode(BBd);
                                AA.add(BB);
                                treedata.put(BBd, i);
                            } else if ((strs[i].charAt(0) == '.') && (strs[i].charAt(1) == '.') && (strs[i].charAt(2) == '.')) {
//                                ii++;
                                CCd =strs[i].substring(3,strs[i].indexOf("\r\n")-14) ;
                                CC = new DefaultMutableTreeNode(CCd);
                                BB.add(CC);
                                treedata.put(CCd, i);
                            }
                        }
                    }
                    DefaultTreeModel model = new DefaultTreeModel(root);
                    tree.setModel(model);
                    tree.setRootVisible(false);
                    tree.addTreeSelectionListener(new TreeSelectionAdapter());

                    frm.setVisible(true);

                    DefaultMutableTreeNode node = AA;
                    if (node != null) {
                        String hskey = (String) node.getUserObject();
                        int hsnom = treedata.get(hskey);
                        tx.setText(strs[hsnom]);
                    }
                }
                catch (Exception aho) {//例外処理
                    tx.setText("エラー");
                }
            }

            if (e.getActionCommand().equals("Save")) {
                JFileChooser filechooser = new JFileChooser("d:\\java_work\\intelliJ\\memo1\\src");
                String directory="";

                int selected = filechooser.showOpenDialog(frm);
                if (selected == JFileChooser.APPROVE_OPTION){
                    File file = filechooser.getSelectedFile();
                    directory = file.getAbsolutePath();
                    frm.setTitle(directory);
                }else if (selected == JFileChooser.CANCEL_OPTION){
                }else if (selected == JFileChooser.ERROR_OPTION){
                }

                String moji = tx.getText();
                try {
                    Files.write(Paths.get(directory), moji.getBytes(StandardCharsets.UTF_8.name()));
                } catch (Exception aho) {
                }
            }

            if (e.getActionCommand().equals("Ver")) {
                Dialog alert = new Dialog(frm, "バージョン情報");
                alert.add(new JLabel("メモ Ver.0.0"));
                alert.setSize(200, 100);
                alert.setVisible(true);
                alert.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        alert.dispose();
                    }
                });
            }
        }
    }
    
    class TreeSelectionAdapter implements TreeSelectionListener {
        public void valueChanged (TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null) {
                String hskey = (String) node.getUserObject();
                int hsnom = treedata.get(hskey);
                tx.setText(strs[hsnom]);
            }
        }
    }

}
