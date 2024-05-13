package consoleTasks;

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
import java.util.ArrayList;

public class PlotFrame extends JFrame {
    private JPanel contentPane;
    private ArrayList points = null;
    private ArrayList pointsDef = null;
    private XYSeries series;
    private XYSeries defSeries;
    private String title;

    public PlotFrame(ArrayList <Point2D> points, ArrayList <Point2D> pointsDef, String title){
        this.points = points;
        this.pointsDef = pointsDef;
        this.title = title;
        setResizable(false);
        setTitle("Plot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JFreeChart chart = createChart(points, pointsDef, title);
        ChartPanel chartPanel = new ChartPanel(chart);
        contentPane.add(chartPanel, BorderLayout.CENTER);

    }

    private JFreeChart createChart(ArrayList <Point2D> points, ArrayList <Point2D> pointsDef, String title) {
        series = new XYSeries("Function");
        defSeries = new XYSeries("Def");
        double start = -9.0;
        double stop = 9.0;



        for (Point2D point: points) {
            series.add(point.getX(), point.getY());
        }

        for (Point2D point: pointsDef) {
            defSeries.add(point.getX(), point.getY());
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(defSeries);
        JFreeChart chart = ChartFactory.createXYLineChart(title,
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL, true,
                true,
                false
        );
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray); // Меняем фон на светло-серый
        plot.setAxisOffset(new RectangleInsets(7.0, 7.0, 7.0, 7.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        return chart;
    }

}

