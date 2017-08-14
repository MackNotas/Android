package giovannicornachini.macknotas.br;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FillFormatter;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import giovannicornachini.macknotas.br.Entidades.DesempenhoPessoal;
import giovannicornachini.macknotas.br.dao.AjustesDAO;
import giovannicornachini.macknotas.br.dao.InformacaoPessoalDAO;


public class InformacaoPessoal extends ActionBarActivity {

    DesempenhoPessoal desempenhoPessoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao_pessoal);

        //Cor Menu
        try {
            android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
            bar.setBackgroundDrawable(new ColorDrawable(0xffbb0505));
            bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
            bar.setDisplayShowTitleEnabled(true);
        }catch (Exception e){

        }

        Bundle bundle = InformacaoPessoal.this.getIntent().getExtras();
        final String login = bundle.getString("login");
        final String senha = bundle.getString("senha");
        final String unidade = bundle.getString("unidade");

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.button_red_mack));
        pDialog.setTitleText("Carregando...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                desempenhoPessoal = InformacaoPessoalDAO.getDesempenhoPessoal(login, senha, unidade);

                InformacaoPessoal.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(desempenhoPessoal.getEntries().isEmpty()){
                            pDialog.cancel();
                            AlertDialog alertDialog = new AlertDialog.Builder(InformacaoPessoal.this).create();
                            alertDialog.setTitle("Ops, deu ruim!");
                            alertDialog.setMessage("Por favor, verifique sua conexão com a internet ou tente mais tarde.");
                            alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            alertDialog.setIcon(R.mipmap.ic_launcher);
                            alertDialog.show();


                        }else{
                            createDesempenhoChart(desempenhoPessoal.getEntries(),
                                    desempenhoPessoal.getLabels(), desempenhoPessoal.getMediaGeral());
                            pDialog.cancel();
                        }
                    }
                });
            }

        }).start();

        //createDesempenhoChart(entries, labels);



    }

    public void createDesempenhoChart(ArrayList<Entry> entries, ArrayList<String> labels, Float mediaGeral){
        LineDataSet dataset = new LineDataSet(entries, "Desempenho semestral");
        //dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        //dataset.enableDashedLine(10f, 5f, 0f);
        dataset.setColor(getResources().getColor(R.color.button_red_mack));
        dataset.setCircleColor(Color.BLACK);
        dataset.setLineWidth(10f);
        dataset.setCircleSize(10f);
        dataset.setDrawCircleHole(true);
        dataset.setValueTextSize(12f);
        dataset.setFillColor(Color.BLACK);


        LineChart chart = (LineChart) findViewById(R.id.desempenhoChart);

        LineData data = new LineData(labels, dataset);

        chart.setData(data);

        chart.setDescription("MackNotas");
        chart.setDrawGridBackground(false);

        chart.setHighlightEnabled(true);
        // enable touch gestures
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(true);
        chart.animateY(1250);

        LimitLine line = new LimitLine(mediaGeral,String.valueOf(mediaGeral));
        line.setLineWidth(3f);
        line.setTextSize(12);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(line);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_informacao_pessoal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.act_fechar){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
