package mobile.android.jx.calc;

import java.io.StringReader;

import mobile.android.jx.calc.parser.CalcParser;
import mobile.android.jx.calc.parser.CalcParserTokenManager;
import mobile.android.jx.calc.parser.SimpleCharStream;
import mobile.android.jx.calc.parser.TokenMgrError;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class Calc extends Activity implements OnClickListener
{
	private GridView gvButtons;
	private TextView tvExpression;
	private TextView tvResult;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gvButtons = (GridView) findViewById(R.id.gridview_buttons);
		tvExpression = (TextView) findViewById(R.id.textview_expression);
		tvResult = (TextView) findViewById(R.id.textview_result);

		gvButtons.setAdapter(new GridAdapter(this));

	}

	@Override
	public void onClick(View view)
	{

		Button button = (Button) view;
		String text = button.getText().toString();
		if (text.equals("="))
		{
			CalcParser parser = new CalcParser(tvExpression.getText()
					.toString());
			try
			{

				tvResult.setText("= " + String.valueOf(parser.expr()));

			}		
			catch (Exception e)
			{
				tvResult.setText(e.getMessage());
			}

		}
		else if (text.equals("退格"))
		{
			SimpleCharStream simpleCharStream = new SimpleCharStream(
					new StringReader(tvExpression.getText().toString()));

			CalcParserTokenManager calcParserTokenManager = new CalcParserTokenManager(
					simpleCharStream);

			String s = "";
			String first = "";
			String second = "";
			first = calcParserTokenManager.getNextToken().image;
			while (!first.equals(""))
			{
				second = first;
				first = calcParserTokenManager.getNextToken().image;
				if (!first.equals(""))
					s += second;

			}

			tvExpression.setText(s);

		}
		else if (text.equals("清除"))
		{
			tvExpression.setText("");
			tvResult.setText("");
		}
		else
		{
			tvExpression.append(text);
		}

	}
}