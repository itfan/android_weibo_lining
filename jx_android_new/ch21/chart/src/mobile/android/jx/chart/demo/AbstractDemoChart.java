
package mobile.android.jx.chart.demo;

import java.util.Date;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public abstract class AbstractDemoChart implements IChart {

  protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
      List<double[]> yValues) {
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    int length = titles.length;
    for (int i = 0; i < length; i++) {
      XYSeries series = new XYSeries(titles[i]);
      double[] xV = xValues.get(i);
      double[] yV = yValues.get(i);
      int seriesLength = xV.length;
      for (int k = 0; k < seriesLength; k++) {
        series.add(xV[k], yV[k]);
      }
      dataset.addSeries(series);
    }
    return dataset;
  }

  protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    renderer.setAxisTitleTextSize(16);
    renderer.setChartTitleTextSize(20);
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    renderer.setPointSize(5f);
    renderer.setMargins(new int[] { 20, 30, 15, 0 });
    int length = colors.length;
    for (int i = 0; i < length; i++) {
      XYSeriesRenderer r = new XYSeriesRenderer();
      r.setColor(colors[i]);
      r.setPointStyle(styles[i]);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }


  protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
      int labelsColor) {
    renderer.setChartTitle(title);
    renderer.setXTitle(xTitle);
    renderer.setYTitle(yTitle);
    renderer.setXAxisMin(xMin);
    renderer.setXAxisMax(xMax);
    renderer.setYAxisMin(yMin);
    renderer.setYAxisMax(yMax);
    renderer.setAxesColor(axesColor);
    renderer.setLabelsColor(labelsColor);
  }


  protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
      List<double[]> yValues) {
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    int length = titles.length;
    for (int i = 0; i < length; i++) {
      TimeSeries series = new TimeSeries(titles[i]);
      Date[] xV = xValues.get(i);
      double[] yV = yValues.get(i);
      int seriesLength = xV.length;
      for (int k = 0; k < seriesLength; k++) {
        series.add(xV[k], yV[k]);
      }
      dataset.addSeries(series);
    }
    return dataset;
  }


  protected CategorySeries buildCategoryDataset(String title, double[] values) {
    CategorySeries series = new CategorySeries(title);
    int k = 0;
    for (double value : values) {
      series.add("Project " + ++k, value);
    }

    return series;
  }


  protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
      List<String[]> titles, List<double[]> values) {
    MultipleCategorySeries series = new MultipleCategorySeries(title);
    int k = 0;
    for (double[] value : values) {
      series.add(2007 + k + "", titles.get(k), value);
      k++;
    }
    return series;
  }

  protected DefaultRenderer buildCategoryRenderer(int[] colors) {
    DefaultRenderer renderer = new DefaultRenderer();
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    renderer.setMargins(new int[] { 20, 30, 15, 0 });
    for (int color : colors) {
      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
      r.setColor(color);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }


  protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    int length = titles.length;
    for (int i = 0; i < length; i++) {
      CategorySeries series = new CategorySeries(titles[i]);
      double[] v = values.get(i);
      int seriesLength = v.length;
      for (int k = 0; k < seriesLength; k++) {
        series.add(v[k]);
      }
      dataset.addSeries(series.toXYSeries());
    }
    return dataset;
  }


  protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    renderer.setAxisTitleTextSize(16);
    renderer.setChartTitleTextSize(20);
    renderer.setLabelsTextSize(15);
    renderer.setLegendTextSize(15);
    int length = colors.length;
    for (int i = 0; i < length; i++) {
      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
      r.setColor(colors[i]);
      renderer.addSeriesRenderer(r);
    }
    return renderer;
  }

}