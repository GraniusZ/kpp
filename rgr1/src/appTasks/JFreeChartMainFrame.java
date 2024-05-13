package appTasks;

import edu.hws.jcm.awt.*;
import edu.hws.jcm.data.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFreeChartMainFrame extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldFun;
    private JTextField textFieldStart;
    private JTextField textFieldStop;
    private JTextField textFieldStep;
    private JTextField textFieldA;
    private XYSeries series;
    private XYSeries defSeries;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFreeChartMainFrame frame = new JFreeChartMainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JFreeChartMainFrame() {
        setResizable(false);
        setTitle("Plot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        JPanel panelButtons = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelButtons.getLayout();
        flowLayout.setHgap(15);
        contentPane.add(panelButtons, BorderLayout.SOUTH);
        JButton btnNewButtonPlot = new JButton("Plot");

        btnNewButtonPlot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double start = -9.0;
                double stop = 9.0;
                double step = 0.01;
                double a = 1.0;

                Parser p = new Parser();
                Variable xVal = new Variable("x");
                Variable yVal = new Variable("a");
                a = Double.parseDouble(textFieldA.getText());
                yVal.setVal(a);
                p.add(xVal);
                p.add(yVal);
                ExpressionInput input = new ExpressionInput(textFieldFun.getText(),p);
                Function func = input.getFunction(xVal);
                Function deriv = func.derivative(1);


                start = Double.parseDouble(textFieldStart.getText());
                stop = Double.parseDouble(textFieldStop.getText());
                step = Double.parseDouble(textFieldStep.getText());


                if(start >= stop){
                    start = -9.0;
                    stop = 9.0;
                    textFieldStart.setText(Double.toString(start));
                    textFieldStop.setText(Double.toString(stop));
                }

                if(step <= 0){
                    step = 0.01;
                    textFieldStep.setText(Double.toString(step));
                }

                series.clear();
                defSeries.clear();

                for (double x = start; x < stop; x += step) {
                    series.add(x, func.getVal(new double[]{x}));
                }
                for (double x = start; x < stop; x += step) {
                    defSeries.add(x, deriv.getVal(new double[]{x}));
                }
            }
        });
        panelButtons.add(btnNewButtonPlot);
        JButton btnNewButtonExit = new JButton("Exit");
        btnNewButtonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panelButtons.add(btnNewButtonExit);
        JPanel panelData = new JPanel();
        contentPane.add(panelData, BorderLayout.NORTH);



        JLabel lblNewLabel = new JLabel("y=");
        panelData.add(lblNewLabel);
        textFieldFun = new JTextField();
        textFieldFun.setText("x^2");
        panelData.add(textFieldFun);
        textFieldFun.setColumns(6);

        JLabel lblNewLabel2 = new JLabel("Start:");
        panelButtons.add(lblNewLabel2);
        textFieldStart = new JTextField();
        textFieldStart.setText("-9.0");
        panelButtons.add(textFieldStart);
        textFieldStart.setColumns(3);

        JLabel lblNewLabel3 = new JLabel("Stop:");
        panelButtons.add(lblNewLabel3);
        textFieldStop = new JTextField();
        textFieldStop.setText("9.0");
        panelButtons.add(textFieldStop);
        textFieldStop.setColumns(3);


        textFieldStep = new JTextField();
        textFieldStep.setText("0.05");
        panelButtons.add(textFieldStep);
        textFieldStep.setColumns(3);

        // Добавление поля в панель
        panelData.add(new JLabel("Step:")); // Добавление метки
        panelData.add(textFieldStep); // Добавление текстового поля
        JLabel lblNewLabel5 = new JLabel("a:");
        panelData.add(lblNewLabel5);
        textFieldA = new JTextField();
        textFieldA.setText("1");
        panelData.add(textFieldA);
        textFieldA.setColumns(3);

        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        contentPane.add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart() {
        series = new XYSeries("Function");
        defSeries = new XYSeries("Def");
        double start = -9.0;
        double stop = 9.0;
        double step = 0.01;

        Parser p = new Parser();
        Variable xVal = new Variable("x");
        Variable aVal = new Variable("a");
        p.add(xVal);
        p.add(aVal);

        ExpressionInput input = new ExpressionInput(textFieldFun.getText(),p);
        Function func = input.getFunction(xVal);
        Function deriv = func.derivative(1);

        for (double x = start; x < stop; x += step) {
            series.add(x, func.getVal(new double[]{x}));
        }
        for (double x = start; x < stop; x += step) {
            defSeries.add(x, deriv.getVal(new double[]{x}));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(defSeries);
        JFreeChart chart = ChartFactory.createXYLineChart("Plot",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL, true,
                true,
                false
        );
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(7.0, 7.0, 7.0, 7.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        return chart;
    }

}