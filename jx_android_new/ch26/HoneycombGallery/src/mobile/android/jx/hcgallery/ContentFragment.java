
package mobile.android.jx.hcgallery;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;


public class ContentFragment extends Fragment {
    private View mContentView;

    private Bitmap mBitmap = null;

    private ActionMode mCurrentActionMode;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.content_welcome, null);
        final ImageView imageView = (ImageView) mContentView.findViewById(R.id.image);
        mContentView.setDrawingCacheEnabled(false);

        mContentView.setOnDragListener(new View.OnDragListener() {
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        mContentView.setBackgroundColor(
                                getResources().getColor(R.color.drag_active_color));
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        mContentView.setBackgroundColor(Color.TRANSPARENT);
                        break;

                    case DragEvent.ACTION_DRAG_STARTED:
                        return processDragStarted(event);

                    case DragEvent.ACTION_DROP:
                        mContentView.setBackgroundColor(Color.TRANSPARENT);
                        return processDrop(event, imageView);
                }
                return false;
            }
        });

   
        final Activity activity = getActivity();
        mContentView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    public void onSystemUiVisibilityChange(int visibility) {
                        ActionBar actionBar = activity.getActionBar();
                        if (actionBar != null) {
                            mContentView.setSystemUiVisibility(visibility);
                            if (visibility == View.STATUS_BAR_VISIBLE) {
                                actionBar.show();
                            } else {
                                actionBar.hide();
                            }
                        }
                    }
                });


        mContentView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (mContentView.getSystemUiVisibility() == View.STATUS_BAR_VISIBLE) {
                    mContentView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
                } else {
                    mContentView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
                }
            }
        });

        mContentView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (mCurrentActionMode != null) {
                    return false;
                }

                mCurrentActionMode = getActivity().startActionMode(
                        mContentSelectionActionModeCallback);
                mContentView.setSelected(true);
                return true;
            }
        });

        return mContentView;
    }

    boolean processDragStarted(DragEvent event) {

        ClipDescription clipDesc = event.getClipDescription();
        if (clipDesc != null) {
            return clipDesc.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        }
        return false;
    }

    boolean processDrop(DragEvent event, ImageView imageView) {

        ClipData data = event.getClipData();
        if (data != null) {
            if (data.getItemCount() > 0) {
                Item item = data.getItemAt(0);
                String textData = (String) item.getText();
                if (textData != null) {
                    StringTokenizer tokenizer = new StringTokenizer(textData, "||");
                    if (tokenizer.countTokens() != 2) {
                        return false;
                    }
                    int category = -1;
                    int entryId = -1;
                    try {
                        category = Integer.parseInt(tokenizer.nextToken());
                        entryId = Integer.parseInt(tokenizer.nextToken());
                    } catch (NumberFormatException exception) {
                        return false;
                    }
                    updateContentAndRecycleBitmap(category, entryId);
           
                    TitlesFragment titlesFrag = (TitlesFragment)
                            getFragmentManager().findFragmentById(R.id.frag_title);
                    titlesFrag.selectPosition(entryId);
                    return true;
                }
            }
        }
        return false;
    }

    void updateContentAndRecycleBitmap(int category, int position) {
        if (mCurrentActionMode != null) {
            mCurrentActionMode.finish();
        }

        if (mBitmap != null) {

            mBitmap.recycle();
        }

        mBitmap = Directory.getCategory(category).getEntry(position)
                .getBitmap(getResources());
        ((ImageView) getView().findViewById(R.id.image)).setImageBitmap(mBitmap);
    }

    void shareCurrentPhoto() {
        File externalCacheDir = getActivity().getExternalCacheDir();
        if (externalCacheDir == null) {
            Toast.makeText(getActivity(), "Error writing to USB/external storage.",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        final File noMediaFile = new File(externalCacheDir, ".nomedia");
        try {
            noMediaFile.createNewFile();
        } catch (IOException e) {
        }

        final File tempFile = new File(externalCacheDir, "tempfile.jpg");

        new AsyncTask<Void, Void, Boolean>() {

            protected Boolean doInBackground(Void... voids) {
                try {
                    FileOutputStream fo = new FileOutputStream(tempFile, false);
                    if (!mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fo)) {
                        Toast.makeText(getActivity(), "Error writing bitmap data.",
                                Toast.LENGTH_SHORT).show();
                        return Boolean.FALSE;
                    }
                    return Boolean.TRUE;

                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), "Error writing to USB/external storage.",
                            Toast.LENGTH_SHORT).show();
                    return Boolean.FALSE;
                }
            }

            protected void onPostExecute(Boolean result) {
                if (result != Boolean.TRUE) {
                    return;
                }

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "Share photo"));
            }
        }.execute();
    }

    private ActionMode.Callback mContentSelectionActionModeCallback = new ActionMode.Callback() {
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle(R.string.photo_selection_cab_title);

            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.photo_context_menu, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.share:
                    shareCurrentPhoto();
                    actionMode.finish();
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            mContentView.setSelected(false);
            mCurrentActionMode = null;
        }
    };
}
