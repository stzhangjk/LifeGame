

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by Grady on 2015.12.17.
 */
public class MainFrame extends JFrame {

    Engine engine;

    String title = "Life Game";

    final Color liveColor = Color.BLACK;      //改颜色在这改
    final Color deadColor = Color.WHITE;
    final Font vFont = new Font("微软雅黑",Font.PLAIN,14);

    JPanel[][] map;
    JPanel right;
    JPanel left;
    JButton startBtn;
    JButton pauseBtn;
    JButton resumeBtn;
    JButton stopBtn;

    //用户自定一参数控件
    JTextField widthTxt;
    JTextField heightTxt;

    JSlider widthSlider;
    JSlider heightSlider;
    JTextField rateTxt;
    JSlider rateSlider;
    JTextField originalTxt;
    JSlider originalSlider;

    JLabel widthLab;
    JLabel heightLab;
    JLabel rateLab;
    JLabel originalLab;

    JSlider toLiveSlider;
    JSlider noChangeSlider;

    //展示控件，为了好看→_→
    JProgressBar count;

    Thread thread;

    {
        UIManager.put("ToolTip.font", vFont);

        UIManager.put("Table.font", vFont);

        UIManager.put("TableHeader.font", vFont);

        UIManager.put("TextField.font", vFont);

        UIManager.put("ComboBox.font", vFont);

        UIManager.put("TextField.font", vFont);

        UIManager.put("PasswordField.font", vFont);

        UIManager.put("TextArea.font", vFont);

        UIManager.put("TextPane.font", vFont);

        UIManager.put("EditorPane.font", vFont);

        UIManager.put("FormattedTextField.font", vFont);

        UIManager.put("Button.font", vFont);

        UIManager.put("CheckBox.font", vFont);

        UIManager.put("RadioButton.font", vFont);

        UIManager.put("ToggleButton.font", vFont);

        UIManager.put("ProgressBar.font", vFont);

        UIManager.put("DesktopIcon.font", vFont);

        UIManager.put("TitledBorder.font", vFont);

        UIManager.put("Label.font", vFont);

        UIManager.put("List.font", vFont);

        UIManager.put("TabbedPane.font", vFont);

        UIManager.put("MenuBar.font", vFont);

        UIManager.put("Menu.font", vFont);

        UIManager.put("MenuItem.font", vFont);

        UIManager.put("PopupMenu.font", vFont);

        UIManager.put("CheckBoxMenuItem.font", vFont);

        UIManager.put("RadioButtonMenuItem.font", vFont);

        UIManager.put("Spinner.font", vFont);

        UIManager.put("Tree.font", vFont);

        UIManager.put("ToolBar.font", vFont);

        UIManager.put("OptionPane.messageFont", vFont);

        UIManager.put("OptionPane.buttonFont", vFont);
    }


    public MainFrame(){
        super();
        //setFont(font);

        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 600);

        left = new JPanel();
        right = new JPanel();

        right.setLayout(new GridLayout(100, 100));
        right.setBorder(new TitledBorder("世界"));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(left,BorderLayout.WEST);
        this.getContentPane().add(right,BorderLayout.CENTER);
        //BoxLayout boxLayout = new BoxLayout(left, BoxLayout.Y_AXIS);
        //left.setLayout(boxLayout);
        //JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        
        //splitPane.setDividerSize(1);
        //this.add(splitPane);

        //设置按钮
        startBtn = new JButton("开始");
        startBtn.addActionListener((ActionEvent event) -> {
            try {

                int width;
                int height;
                int rate;
                int original;

                try {
                    width = widthSlider.getValue();
                    height = heightSlider.getValue();
                    rate = Integer.parseInt(rateTxt.getText().trim());
                    original = originalSlider.getValue();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "参数错误");
                    return;
                }


                if (original > width * height) {
                    JOptionPane.showMessageDialog(null, "初代不能超过细胞总数");
                    return;
                }

                right.removeAll();
                right.setLayout(new GridLayout(width, height));

                //构建map
                map = new JPanel[width][height];
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        map[i][j] = new JPanel();
                        map[i][j].setBackground(deadColor);
                        right.add(map[i][j]);
                    }
                }



                engine = new Engine(rate, width, height, original);
                engine.setMap(map);
                engine.setLiveColor(liveColor);
                engine.setDeadColor(deadColor);
                engine.setToLive(toLiveSlider.getValue());
                engine.setNoChange(noChangeSlider.getValue());

                count.setMinimum(0);
                count.setMaximum(width * height);
                count.setValue(original);

                engine.setCount(count);
                engine.init();

                right.updateUI();


                if (thread != null) {
                    if (thread.isAlive()) {
                        thread.interrupt();
                    }
                }
                thread = new Thread(this.engine);
                thread.start();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });

        pauseBtn = new JButton("暂停");
        pauseBtn.addActionListener((ActionEvent event) -> {
            //engine.pause = true;
            thread.suspend();
        });

        resumeBtn = new JButton("继续");
        resumeBtn.addActionListener((ActionEvent event) -> {
            //engine.pause = false;
            //notify();
            thread.resume();
        });

        stopBtn = new JButton("停止");
        stopBtn.addActionListener((ActionEvent event) -> {
            thread.interrupt();
        });






        widthLab = new JLabel("宽");
        widthTxt = new JTextField(10);
        widthTxt.addActionListener((ActionEvent event)->{
            widthSlider.setValue(Integer.parseInt(widthTxt.getText().trim()));
            System.out.println(0000);
        });
        widthSlider = new JSlider(50,150);
        widthSlider.setValue(50);
//        widthSlider.setPaintTrack(true);
//        widthSlider.setPaintTicks(true);
//        widthSlider.setPaintLabels(true);
//        widthSlider.setMajorTickSpacing(10);
//        widthSlider.setMinorTickSpacing(1);
        widthSlider.addChangeListener((ChangeEvent event) -> {
            originalSlider.setMaximum(widthSlider.getValue() * heightSlider.getValue());
            originalSlider.setMajorTickSpacing(originalSlider.getExtent() / 10);
            originalSlider.setMinorTickSpacing(originalSlider.getExtent() / 100);
            //originalSlider.updateUI();

            widthTxt.setText(Integer.toString(widthSlider.getValue()));
        });
        JPanel widthPanel = new JPanel();
        widthPanel.setLayout(new BorderLayout());
        widthPanel.add(widthLab, BorderLayout.NORTH);
        widthPanel.add(widthTxt, BorderLayout.CENTER);
        widthPanel.add(widthSlider, BorderLayout.SOUTH);


        heightLab = new JLabel("高");
        heightTxt = new JTextField(10);
        heightTxt.addActionListener((ActionEvent event)->{
            heightSlider.setValue(Integer.parseInt(heightTxt.getText().trim()));
        });
        heightSlider = new JSlider(50,150);
        heightSlider.setValue(50);
//        heightSlider.setPaintTrack(true);
//        heightSlider.setPaintTicks(true);
//        heightSlider.setPaintLabels(true);
//        heightSlider.setMajorTickSpacing(10);
//        heightSlider.setMinorTickSpacing(1);
        heightSlider.addChangeListener((ChangeEvent event) -> {
            originalSlider.setMaximum(widthSlider.getValue() * heightSlider.getValue());
            originalSlider.setMajorTickSpacing(originalSlider.getExtent() / 10);
            originalSlider.setMinorTickSpacing(originalSlider.getExtent() / 100);
            //originalSlider.updateUI();

            heightTxt.setText(Integer.toString(heightSlider.getValue()));
        });
        JPanel heightPanel = new JPanel();
        heightPanel.setLayout(new BorderLayout());
        heightPanel.add(heightLab, BorderLayout.NORTH);
        heightPanel.add(heightTxt,BorderLayout.CENTER);
        heightPanel.add(heightSlider,BorderLayout.SOUTH);


        rateLab = new JLabel("频率（ms/次）");
        rateTxt = new JTextField(10);
        rateTxt.addActionListener((ActionEvent event)->{
            rateSlider.setValue(Integer.parseInt(rateTxt.getText().trim()));
        });
        rateSlider = new JSlider(50,500);
        rateSlider.addChangeListener((ChangeEvent event)->{
            rateTxt.setText(Integer.toString(rateSlider.getValue()));
            if(engine!=null){
                engine.setRate(rateSlider.getValue());
            }
        });
        JPanel ratePanel = new JPanel();
        ratePanel.setLayout(new BorderLayout());
        ratePanel.add(rateLab, BorderLayout.NORTH);
        ratePanel.add(rateTxt,BorderLayout.CENTER);
        ratePanel.add(rateSlider,BorderLayout.SOUTH);

        originalLab = new JLabel("初代数量");
        originalTxt = new JTextField(10);
        originalTxt.addActionListener((ActionEvent event)->{
            originalSlider.setValue(Integer.parseInt(originalTxt.getText()));
        });
        originalSlider = new JSlider(0,widthSlider.getMinimum()*heightSlider.getMinimum());
        originalSlider.setValue(0);
//        originalSlider.setPaintTrack(true);
//        originalSlider.setPaintTicks(true);
//        originalSlider.setPaintLabels(true);
        originalSlider.setMajorTickSpacing(originalSlider.getExtent() / 10);
        originalSlider.setMinorTickSpacing(originalSlider.getExtent() / 100);
        originalSlider.addChangeListener((ChangeEvent event) -> {
            originalTxt.setText(Integer.toString(originalSlider.getValue()));
        });
        JPanel originalPanel = new JPanel();
        originalPanel.setLayout(new BorderLayout());
        originalPanel.add(originalLab, BorderLayout.NORTH);
        originalPanel.add(originalTxt,BorderLayout.CENTER);
        originalPanel.add(originalSlider,BorderLayout.SOUTH);



        JLabel toLiveLabel = new JLabel("转生条件");
        toLiveSlider = new JSlider(0,8,3);
        toLiveSlider.setPaintLabels(true);
        toLiveSlider.setPaintTicks(true);
        toLiveSlider.setPaintTrack(true);
        toLiveSlider.setMajorTickSpacing(1);
        toLiveSlider.addChangeListener((ChangeEvent event) -> {
            if (engine != null) {
                engine.setToLive(toLiveSlider.getValue());
            }
        });
        JPanel toLivePanel = new JPanel();
        toLivePanel.setLayout(new BorderLayout());
        toLivePanel.add(toLiveLabel, BorderLayout.NORTH);
        toLivePanel.add(toLiveSlider, BorderLayout.CENTER);


        JLabel noChangeLabel = new JLabel("生死状态不变条件");
        noChangeSlider = new JSlider(0,8,2);
        noChangeSlider.setPaintLabels(true);
        noChangeSlider.setPaintTicks(true);
        noChangeSlider.setPaintTrack(true);
        noChangeSlider.setMajorTickSpacing(1);
        noChangeSlider.addChangeListener((ChangeEvent event) -> {
            if (engine != null) {
                engine.setNoChange(noChangeSlider.getValue());
            }
        });
        JPanel noChangePanel = new JPanel();
        noChangePanel.setLayout(new BorderLayout());
        noChangePanel.add(noChangeLabel, BorderLayout.NORTH);
        noChangePanel.add(noChangeSlider, BorderLayout.CENTER);



        JPanel paraPanel = new JPanel();
        paraPanel.setLayout(new BoxLayout(paraPanel, BoxLayout.Y_AXIS));
        paraPanel.setBorder(new TitledBorder("参数设置"));
        paraPanel.add(widthPanel);
        paraPanel.add(heightPanel);
        paraPanel.add(ratePanel);
        paraPanel.add(originalPanel);
        paraPanel.add(toLivePanel);
        paraPanel.add(noChangePanel);



        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 4));
        buttons.setBorder(new TitledBorder("控制"));
        buttons.add(startBtn);
        buttons.add(pauseBtn);
        buttons.add(resumeBtn);
        buttons.add(stopBtn);

        count = new JProgressBar(SwingConstants.VERTICAL);

        JLayeredPane layeredPane = new JLayeredPane();

        JLabel countLabel = new JLabel("000");
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setFont(new Font("Open Sans",Font.PLAIN,80));
        countLabel.setForeground(Color.orange);
        count.addChangeListener((ChangeEvent event) -> {
            countLabel.setText(Integer.toString(count.getValue()));
        });



        layeredPane.add(count, new Integer(200));
        layeredPane.add(countLabel, new Integer(300));
        layeredPane.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                count.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                countLabel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });


        left.setLayout(new BorderLayout());
        left.add(paraPanel, BorderLayout.NORTH);
        left.add(layeredPane, BorderLayout.CENTER);

        left.add(buttons, BorderLayout.SOUTH);
    }


}
