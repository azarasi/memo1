import java.awt.Font;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
//import java.awt.Dialog;
import java.io.*;
import java.awt.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

//import javax.imageio.stream.IIOByteBuffer;
import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.event.*;
//import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Hashtable;

import java.io.BufferedReader;
import java.io.FileInputStream;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class memo1{
    private String lafClassName;
    private JFrame frm;
    private JTextArea tx;
    private JTree tree;
    private Hashtable<String, Integer> treedata;
    private String[] strs;
    private int hsnom;
    
    private int win_width, win_height;
    private String font_name;
    private int font_size;
    private String mem_path;

    protected JComboBox comboFonts;  /* Font選択 */
    protected JComboBox comboSizes;  /* Fontサイズ */

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        memo1 chou = new memo1();
        chou.init();
    }

    protected void init(){
        prop_read();  //property file read
        font_init();

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
    protected void prop_read() {
//        File prop_file_path = new File(get_currentpath() + "mem.properties");
        File prop_file_path = new File("mem.properties");
        if (prop_file_path.exists()) {
            try {
                FileInputStream fis = new FileInputStream("mem.properties");
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                Properties prop = new Properties();
                prop.load(br);

                lafClassName = prop.getProperty("lafClassName");
                win_width = Integer.parseInt(prop.getProperty("win_width"));
                win_height = Integer.parseInt(prop.getProperty("win_height"));
                font_name = prop.getProperty("font_name");
                font_size = Integer.parseInt(prop.getProperty("font_size"));
                mem_path = prop.getProperty("mem_path");

            } catch (Exception aho) {//例外処理
            }
        }else{
            lafClassName = "";
            win_width = 720;
            win_height = 1280;
            font_name = "MS Mincho";
            font_size = 24;
            mem_path = "";
        }
    }
    String get_currentpath(){  //IDEのデバッグモードでは動かない？
        String cp=System.getProperty("java.class.path");
        String fs=System.getProperty("file.separator");
        String acp=(new File(cp)).getAbsolutePath();
//
        int p,q;
        for(p=0;(q=acp.indexOf(fs,p))>=0;p=q+1);
        return acp.substring(0,p);
    }
    protected void font_init() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String familyName[] = ge.getAvailableFontFamilyNames();

    /* フォント選択用コンボボックス */
        comboFonts = new JComboBox(familyName);
        comboFonts.setMaximumSize(comboFonts.getPreferredSize());
        comboFonts.addActionListener(new ActionAdapter());
        comboFonts.setActionCommand("comboFonts");

    /* フォントサイズ選択用コンボボックス */
        comboSizes = new JComboBox(new String[] {"8", "9", "10","11", "12", "14", "16",
                "18", "20", "22", "24", "26", "28", "36", "48", "72"});
        comboSizes.setMaximumSize(comboSizes.getPreferredSize());
        comboSizes.addActionListener(new ActionAdapter());
        comboSizes.setActionCommand("comboSizes");

    }

    protected void set_menu() {
        Font font = new Font("Migu 1C",Font.BOLD,12);
        JMenuBar menubar= new JMenuBar();

        JMenu menu1 = new JMenu("File"); menubar.add(menu1);
        menu1.setFont(font);
        JMenuItem menuitem1 = new JMenuItem("New") ; menu1.add(menuitem1);
        JMenuItem menuitem2 = new JMenuItem("Open"); menu1.add(menuitem2);
        JMenuItem menuitem3 = new JMenuItem("Save"); menu1.add(menuitem3);
        menuitem1.setEnabled(false);

        JMenu menu2 = new JMenu("Edit"); menubar.add(menu2);
        menu2.setFont(font);
        JMenuItem menuitem4 = new JMenuItem("FontSelect"); menu2.add(menuitem4);
        JMenuItem menuitem5 = new JMenuItem("prop read");  menu2.add(menuitem5);
        JMenuItem menuitem6 = new JMenuItem("prop write"); menu2.add(menuitem6);
//        menu2.setEnabled(false);

        menubar.add(Box.createHorizontalGlue());
        JMenu menu3 = new JMenu("Help"); menubar.add(menu3);
        menu3.setFont(font);
        JMenuItem menuitem10 = new JMenuItem("Ver") ; menu3.add(menuitem10);

        menuitem1.addActionListener(new ActionAdapter());
        menuitem2.addActionListener(new ActionAdapter());
        menuitem3.addActionListener(new ActionAdapter());
        menuitem4.addActionListener(new ActionAdapter());
        menuitem5.addActionListener(new ActionAdapter());
        menuitem6.addActionListener(new ActionAdapter());
        menuitem10.addActionListener(new ActionAdapter());

        frm.setJMenuBar(menubar);//メニューバーをセットする。
    }
    protected void tree_init() {
        DefaultMutableTreeNode root = tree_gen(new DefaultMutableTreeNode("JavaDrive"));
        tree = new JTree(root);
        tree.setFont(new Font(font_name,Font.PLAIN,font_size));
        tree.setRootVisible(true);

        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.getViewport().setView(tree);
        scrollPane1.setPreferredSize(new Dimension(win_width*1/3,win_height));
        frm.getContentPane().add(scrollPane1, BorderLayout.WEST);
    }
    protected void tx_init() {
        tx=new JTextArea("これがテキストエリアです。");
        tx.setFont(new Font(font_name,Font.PLAIN,font_size));
        JScrollPane scrollpane2 = new JScrollPane();
        scrollpane2.setViewportView(tx);
        tx.setCaretPosition(0);
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
            //Menu New
            if (e.getActionCommand().equals("New")) {
                tx.setText("これが新規");
            }
            //Menu Open
            if (e.getActionCommand().equals("Open")) {

                JFileChooser filechooser = new JFileChooser(mem_path);
                String directory = "";

                int selected = filechooser.showOpenDialog(frm);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    directory = file.getAbsolutePath();
                    frm.setTitle(file.getName());
                } else if (selected == JFileChooser.CANCEL_OPTION) {
                } else if (selected == JFileChooser.ERROR_OPTION) {
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
                    String AAd, BBd, CCd;
                    for (int i = 0; i < all_lines; i++) {
                        if (strs[i].length() > 0) {
                            if (i == 0) {
                                strs[i] = strs[i] + "\r\n";
                            } else if (i == all_lines - 1) {
                                strs[i] = '.' + strs[i];
                            } else {
                                strs[i] = '.' + strs[i] + "\r\n";
                            }
                            if ((strs[i].charAt(0) == '.') && (strs[i].charAt(1) != '.')) {
                                AAd = strs[i].substring(1, strs[i].indexOf("\r\n") - 14);
                                AA = new DefaultMutableTreeNode(AAd);
                                root.add(AA);
                                treedata.put(AAd, i);
                            } else if ((strs[i].charAt(0) == '.') && (strs[i].charAt(1) == '.') && (strs[i].charAt(2) != '.')) {
                                BBd = strs[i].substring(2, strs[i].indexOf("\r\n") - 14);
                                BB = new DefaultMutableTreeNode(BBd);
                                AA.add(BB);
                                treedata.put(BBd, i);
                            } else if ((strs[i].charAt(0) == '.') && (strs[i].charAt(1) == '.') && (strs[i].charAt(2) == '.')) {
                                CCd = strs[i].substring(3, strs[i].indexOf("\r\n") - 14);
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
                        hsnom = treedata.get(hskey);
                        tx.setText(strs[hsnom]);
                        tx.setCaretPosition(0);
                    }
                } catch (Exception aho) {//例外処理
                    tx.setText("エラー");
                }
            }

            //Menu Save
            if (e.getActionCommand().equals("Save")) {
                JFileChooser filechooser = new JFileChooser(mem_path);
                String directory = "";

                int selected = filechooser.showSaveDialog(frm);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    directory = file.getAbsolutePath();
                    frm.setTitle(file.getName());
                } else if (selected == JFileChooser.CANCEL_OPTION) {
                } else if (selected == JFileChooser.ERROR_OPTION) {
                }

                String moji = "";
                for (String linedt : strs) {
                    moji = moji + linedt;
                }
                
                try {
                    Files.write(Paths.get(directory), moji.getBytes(StandardCharsets.UTF_8.name()));
                } catch (Exception aho) {
                }
            }
            //Menu FontSelect
            if (e.getActionCommand().equals("FontSelect")) {
                JDialog font_Sel = new JDialog(frm,"font select");
                font_Sel.setLayout(new FlowLayout(FlowLayout.LEADING));
                font_Sel.add(comboFonts);
                font_Sel.add(comboSizes);

                font_Sel.setSize(300,100);
                font_Sel.setVisible(true);


                font_Sel.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        font_Sel.dispose();
                    }
                });
            }
            //Menu dialog comboFonts
            if (e.getActionCommand().equals("comboFonts")) {
                /* フォント名変更 */
                font_name = comboFonts.getSelectedItem().toString();
                Font font = new Font(font_name,Font.PLAIN,font_size);
                tree.setFont(font);
                tx.setFont(font);
            }
            //Menu dialog comboSizes
            if (e.getActionCommand().equals("comboSizes")) {
                /* フォントサイズ変更 */
                try{
                    font_size = Integer.parseInt(comboSizes.getSelectedItem().toString());
                }catch (NumberFormatException ex){
                    return;
                }
                Font font = new Font(font_name,Font.PLAIN,font_size);
                tree.setFont(font);
                tx.setFont(font);
            }
            //Menu property read
            if (e.getActionCommand().equals("prop read")) {
                prop_read();
                Font font = new Font(font_name,Font.PLAIN,font_size);
                tree.setFont(font);
                tx.setFont(font);
            }
            //Menu property write
            if (e.getActionCommand().equals("prop write")) {
                try {
                    // FileWriterクラスのオブジェクトを生成する
                    FileOutputStream fws = new FileOutputStream("mem.properties");
                    OutputStreamWriter ws = new OutputStreamWriter(fws,"UTF-8");
                    PrintWriter pw = new PrintWriter(new BufferedWriter(ws));

                    //ファイルに書き込む
                    pw.println("lafClassName = " + lafClassName);
                    pw.println("win_width = " + win_width);
                    pw.println("win_height = " + win_height);
                    pw.println("font_name = " + font_name);
                    pw.println("font_size = " + font_size);
                    pw.println("mem_path = " + mem_path.replace("\\", "\\\\"));

                    //ファイルを閉じる
                    pw.close();
                } catch (IOException f) {
                    f.printStackTrace();
                }

            }
            //Menu Ver
            if (e.getActionCommand().equals("Ver")) {
                JDialog alert = new JDialog(frm, "バージョン情報");
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
                strs[hsnom] = tx.getText().replaceAll("\n", "\r\n").replaceAll("\r\r","\r");

                String hskey = (String) node.getUserObject();
                hsnom = treedata.get(hskey);
                tx.setText(strs[hsnom]);
                tx.setCaretPosition(0);
            }
        }
    }

}
