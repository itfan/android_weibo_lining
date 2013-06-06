
package mobile.android.jx.hcgallery;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RemoteViews;

public class MainActivity extends Activity implements ActionBar.TabListener {

    private static final int NOTIFICATION_DEFAULT = 1;
    private static final String ACTION_DIALOG = "mobile.android.jx.hcgallery.action.DIALOG";

    private View mActionBarView;
    private Animator mCurrentTitlesAnimator;
    private String[] mToggleLabels = {"Show Titles", "Hide Titles"};
    private int mLabelIndex = 1;
    private int mThemeId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.getInt("theme", -1) != -1) {
            mThemeId = savedInstanceState.getInt("theme");
            this.setTheme(mThemeId);
        }

        setContentView(R.layout.main);

        Directory.initializeDirectory();

        ActionBar bar = getActionBar();

        int i;
        for (i = 0; i < Directory.getCategoryCount(); i++) {
            bar.addTab(bar.newTab().setText(Directory.getCategory(i).getName())
                    .setTabListener(this));
        }

        mActionBarView = getLayoutInflater().inflate(
                R.layout.action_bar_custom, null);

        bar.setCustomView(mActionBarView);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayShowHomeEnabled(true);

  
        if(savedInstanceState != null) {
            int category = savedInstanceState.getInt("category");
            bar.selectTab(bar.getTabAt(category));
        }
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        TitlesFragment titleFrag = (TitlesFragment) getFragmentManager()
                .findFragmentById(R.id.frag_title);
        titleFrag.populateTitles(tab.getPosition());

        titleFrag.selectPosition(0);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.camera:
            Intent intent = new Intent(this, CameraSample.class);
            intent.putExtra("theme", mThemeId);
            startActivity(intent);
            return true;

        case R.id.toggleTitles:
            toggleVisibleTitles();
            return true;

        case R.id.toggleTheme:
            if (mThemeId == R.style.AppTheme_Dark) {
                mThemeId = R.style.AppTheme_Light;
            } else {
                mThemeId = R.style.AppTheme_Dark;
            }
            this.recreate();
            return true;

        case R.id.showDialog:
            showDialog("This is indeed an awesome dialog.");
            return true;

        case R.id.showStandardNotification:
            showNotification(false);
            return true;

        case R.id.showCustomNotification:
            showNotification(true);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void toggleVisibleTitles() {

        final FragmentManager fm = getFragmentManager();
        final TitlesFragment f = (TitlesFragment) fm
                .findFragmentById(R.id.frag_title);
        final View titlesView = f.getView();
        mLabelIndex = 1 - mLabelIndex;


        final boolean isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;

        final boolean shouldShow = f.isHidden() || mCurrentTitlesAnimator != null;


        if (mCurrentTitlesAnimator != null)
            mCurrentTitlesAnimator.cancel();


        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                titlesView,
                PropertyValuesHolder.ofInt(
                        isPortrait ? "bottom" : "right",
                        shouldShow ? getResources().getDimensionPixelSize(R.dimen.titles_size)
                                   : 0),
                PropertyValuesHolder.ofFloat("alpha", shouldShow ? 1 : 0)
        );

        
        final ViewGroup.LayoutParams lp = titlesView.getLayoutParams();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                if (isPortrait) {
                    lp.height = (Integer) valueAnimator.getAnimatedValue();
                } else {
                    lp.width = (Integer) valueAnimator.getAnimatedValue();
                }
                titlesView.setLayoutParams(lp);
            }
        });

        if (shouldShow) {
            fm.beginTransaction().show(f).commit();
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    mCurrentTitlesAnimator = null;
                }
            });

        } else {
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                boolean canceled;

                @Override
                public void onAnimationCancel(Animator animation) {
                    canceled = true;
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (canceled)
                        return;
                    mCurrentTitlesAnimator = null;
                    fm.beginTransaction().hide(f).commit();
                }
            });
        }


        objectAnimator.start();
        mCurrentTitlesAnimator = objectAnimator;

        invalidateOptionsMenu();


        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ACTION_DIALOG.equals(intent.getAction())) {
            showDialog(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    void showDialog(String text) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        DialogFragment newFragment = MyDialogFragment.newInstance(text);

        newFragment.show(ft, "dialog");
    }

    void showNotification(boolean custom) {
        final Resources res = getResources();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_notify_example)
                .setAutoCancel(true)
                .setTicker(getString(R.string.notification_text))
                .setContentIntent(getDialogPendingIntent("Tapped the notification entry."));

        if (custom) {
           
            RemoteViews layout = new RemoteViews(getPackageName(), R.layout.notification);
            layout.setTextViewText(R.id.notification_title, getString(R.string.app_name));
            layout.setOnClickPendingIntent(R.id.notification_button,
                    getDialogPendingIntent("Tapped the 'dialog' button in the notification."));
            builder.setContent(layout);

          
            Bitmap largeIconTemp = BitmapFactory.decodeResource(res,
                    R.drawable.notification_default_largeicon);
            Bitmap largeIcon = Bitmap.createScaledBitmap(
                    largeIconTemp,
                    res.getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                    res.getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                    false);
            largeIconTemp.recycle();

            builder.setLargeIcon(largeIcon);

        } else {
            builder
                    .setNumber(7) // An example number.
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notification_text));
        }

        notificationManager.notify(NOTIFICATION_DEFAULT, builder.getNotification());
    }

    PendingIntent getDialogPendingIntent(String dialogText) {
        return PendingIntent.getActivity(
                this,
                dialogText.hashCode(), 
                                    
                new Intent(ACTION_DIALOG)
                        .putExtra(Intent.EXTRA_TEXT, dialogText)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(1).setTitle(mToggleLabels[mLabelIndex]);
        return true;
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        ActionBar bar = getActionBar();
        int category = bar.getSelectedTab().getPosition();
        outState.putInt("category", category);
        outState.putInt("theme", mThemeId);
    }


    public static class MyDialogFragment extends DialogFragment {

        public static MyDialogFragment newInstance(String title) {
            MyDialogFragment frag = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putString("text", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String text = getArguments().getString("text");

            return new AlertDialog.Builder(getActivity())
                    .setTitle("A Dialog of Awesome")
                    .setMessage(text)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }
                    )
                    .create();
        }
    }
}
