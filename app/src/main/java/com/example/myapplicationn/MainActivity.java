package com.example.myapplicationn;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private StringBuilder currentNumber;
    private double operand1;
    private double operand2;
    private boolean isOperatorClicked;
    private char operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);
        currentNumber = new StringBuilder();
        isOperatorClicked = false;
    }

    private static final long DISPLAY_DURATION_MS = 2000; // Duración en milisegundos para mostrar los números ingresados

    private Timer timer;

    public void onNumberClick(View view) {
        Button button = (Button) view;
        currentNumber.append(button.getText().toString());
        textViewResult.setText(currentNumber.toString());

        // Ajustar el tamaño del texto para que quepa en el TextView
        adjustTextSize();
    }

    private void adjustTextSize() {
        float textSize = getResources().getDimension(R.dimen.result_text_size); // Tamaño de texto inicial
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        float textWidth = textPaint.measureText(currentNumber.toString());
        float viewWidth = textViewResult.getWidth();

        // Reducir el tamaño del texto si es necesario
        while (textWidth > viewWidth) {
            textSize -= 1;
            textPaint.setTextSize(textSize);
            textWidth = textPaint.measureText(currentNumber.toString());
        }

        textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }


    public void onOperatorClick(View view) {
        Button button = (Button) view;
        operator = button.getText().toString().charAt(0);

        // Verificar si currentNumber está vacío
        if (currentNumber.length() == 0) {
            currentNumber.append("0");
        }

        operand1 = Double.parseDouble(currentNumber.toString());
        currentNumber.setLength(0);
        isOperatorClicked = true;
    }

    public void onEqualsClick(View view) {
        if (isOperatorClicked) {
            if (currentNumber.length() == 0) {
                currentNumber.append("0");
            }
            operand2 = Double.parseDouble(currentNumber.toString());
            double result = calculateResult();

            // Crear el AlertDialog para mostrar el resultado temporalmente
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.valueOf(result));
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            // Esperar 1 segundo antes de cerrar el AlertDialog y restaurar el contenido
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    textViewResult.setText(currentNumber.toString());
                }
            }, 1000);

            currentNumber.setLength(0);
            currentNumber.append(result);
            isOperatorClicked = false;
        }
    }

    private double calculateResult() {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            default:
                return 0;
        }
    }

    public void onClearClick(View view) {
        currentNumber.setLength(0);
        textViewResult.setText("0");
        isOperatorClicked = false;
    }
}
