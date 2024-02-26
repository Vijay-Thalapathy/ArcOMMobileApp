package com.example.arcomdriver.helper;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.arcomdriver.R;
import com.example.arcomdriver.order.Activity_OrdersHistory;
import com.example.arcomdriver.printer.ThermalPrint;
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity;

import java.io.File;
import java.net.URLConnection;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 20 Jan 2023*/
public class ShareInvoicePdfViewer extends PDFViewerActivity {

    String json_p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            json_p = extras.getString("json_p");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Invoice Details");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.colorAccent)));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pdf_viewer, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           // finish();
            Intent intent = new Intent(ShareInvoicePdfViewer.this, Activity_OrdersHistory.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuPrintPdf) {
       /*     File fileToPrint = getPdfFile();
            if (fileToPrint == null || !fileToPrint.exists()) {
                Toast.makeText(this, "Generated File is null or does not exist!", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            PrintAttributes.Builder printAttributeBuilder = new PrintAttributes.Builder();
            printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
            printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

            PDFUtil.printPdf(ShareInvoicePdfViewer.this, fileToPrint, printAttributeBuilder.build());*/

            Bundle bundle = new Bundle();
            bundle.putString("Print",json_p.toString());
            bundle.putString("Flag","1");
            Intent in = new Intent(ShareInvoicePdfViewer.this, ThermalPrint.class);
            in.putExtras(bundle);
            startActivity(in);

        } else if (item.getItemId() == R.id.menuSharePdf) {
            File fileToShare = getPdfFile();
            if (fileToShare == null || !fileToShare.exists()) {
                Toast.makeText(this, "Generated File is null or does not exist!", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);

            Uri apkURI = FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext()
                            .getPackageName() + ".provider", fileToShare);
            intentShareFile.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(fileToShare.getName()));
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intentShareFile.putExtra(Intent.EXTRA_STREAM,
                    apkURI);

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent intent = new Intent(ShareInvoicePdfViewer.this, Activity_OrdersHistory.class);
        startActivity(intent);
    }
}

