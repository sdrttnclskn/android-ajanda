package com.sdrttnclskn.ajanda;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.sdrttnclskn.ajanda.adapters.MainFragmentPagerAdapter;
import com.sdrttnclskn.ajanda.recognition.RecognitionManager;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity implements RecognitionListener , RuntimePermissionListener {

    private static final int REQUEST_CODE = 1001;
    private FragmentPagerAdapter adapterViewPager;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private RecognitionManager recognitionManager;
    private ViewPager mVpPager;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recognitionManager = new RecognitionManager(this);

        mVpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MainFragmentPagerAdapter(fragmentManager);
        mVpPager.setAdapter(adapterViewPager);

        act = this;

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.pending)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.done)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.all)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MainActivity.this.adapterViewPager.notifyDataSetChanged();
                mVpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        mVpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                MainActivity.this.adapterViewPager.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if( state == ViewPager.SCROLL_STATE_SETTLING){
                    MainActivity.this.adapterViewPager.notifyDataSetChanged();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewTaskActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(recognitionManager != null)
        {
            recognitionManager.destroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(recognitionManager != null)
        {
            recognitionManager.destroy();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        try
        {
            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    final View microphoneButton = act.findViewById(R.id.microphoneButton);
                    if(microphoneButton != null)
                    {
                        microphoneButton.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return false;
                            }
                        });
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Yapım Aşamasında", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId()==R.id.microphoneButton){
            startRecognition();
        }

        return super.onOptionsItemSelected(item);
    }

    @AskPermission(RECORD_AUDIO)
    private void startRecognition() {
        recognitionManager.start();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

        switch (error)
        {
            case SpeechRecognizer.ERROR_AUDIO:
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                break;
            case SpeechRecognizer.ERROR_SERVER:
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                break;
        }

        Toast.makeText(this, "Ses tanıma yapılırken bir hata oluştu. Hata kodu : " + error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> textMatchList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if(textMatchList != null && !textMatchList.isEmpty())
        {
            String result = textMatchList.get(0).toLowerCase();

            if(result.contains(getResources().getString(R.string.pending))) {
                mVpPager.setCurrentItem(0);
            } else if (result.contains(getResources().getString(R.string.done))) {
                mVpPager.setCurrentItem(1);
            } else if (result.contains(getResources().getString(R.string.all))) {
                mVpPager.setCurrentItem(2);
            } else if (result.contains(getResources().getString(R.string.title_activity_new_task))) {
                mVpPager.setCurrentItem(3);
            } else if (result.contains(getResources().getString(R.string.placeholder_title))) {
                mVpPager.setCurrentItem(4);
            } else if (result.contains(getResources().getString(R.string.placeholder_date))) {
                mVpPager.setCurrentItem(5);
            } else if (result.contains(getResources().getString(R.string.placeholder_description))) {
                mVpPager.setCurrentItem(6);
            } else if (result.contains(getResources().getString(R.string.action_time))) {
                mVpPager.setCurrentItem(7);
            } else if (result.contains(getResources().getString(R.string.title_activity_view_task))) {
                mVpPager.setCurrentItem(8);
            } else if (result.contains(getResources().getString(R.string.action_delete))) {
                mVpPager.setCurrentItem(9);
            } else if (result.contains(getResources().getString(R.string.action_edit)) ) {
                mVpPager.setCurrentItem(10);
            } else if (result.contains(getResources().getString(R.string.action_show))) {
                mVpPager.setCurrentItem(11);
            }else if (result.contains(getResources().getString(R.string.action_cancel))) {
                mVpPager.setCurrentItem(12);
            }

            Toast.makeText(this, "Tanıma sonucu : " + textMatchList.get(0), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void onShowPermissionRationale(List<String> permissionList, final RuntimePermissionRequest permissionRequest) {

        new AlertDialog.Builder(this, R.style.AppCompatProgressDialogStyle).setTitle("İzine İhtiyaç Var!")
                .setMessage("Ses tanıma yapabilmek için Mikrofon iznine ihtiyacım var.")
                .setCancelable(true)
                .setNegativeButton("Hayır Teşekkkürler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Tekrar Dene", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionRequest.retry();
                    }
                })
                .show();
    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> deniedPermissionList) {

        new AlertDialog.Builder(this, R.style.AppCompatProgressDialogStyle).setTitle("Ayarlara Giderek İzin Ver")
                .setMessage("Ses tanıma yapabilmek için Mikrofon iznine ihtiyacım var.")
                .setCancelable(true)
                .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 1);

                        dialog.dismiss();
                    }
                }).show();

    }
}
