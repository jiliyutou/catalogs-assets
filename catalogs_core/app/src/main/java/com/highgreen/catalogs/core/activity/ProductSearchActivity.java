package com.highgreen.catalogs.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.highgreen.catalogs.core.MainApplication;
import com.highgreen.catalogs.core.R;
import com.highgreen.catalogs.core.adapter.ProductGridAdapter;
import com.highgreen.catalogs.core.bean.ProductItem;
import com.highgreen.catalogs.core.database.SearchDataBaseManager;
import com.highgreen.catalogs.core.upyun.UpYun.FolderItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruantihong on 5/21/16.
 */
public class ProductSearchActivity extends Activity {
    private static final String TAG = "ProductSearchActivity";
    private SearchDataBaseManager mSearchDataBaseManager;
    private HashMap<String, String> mLocalData;

    private ProductGridAdapter mProductGridAdapter;
    private List<ProductItem> mQureyResult = new ArrayList<ProductItem>();
    private ProgressBar mProgressBar;
    private TextView mProgressTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product_search_activity);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        mSearchDataBaseManager = new SearchDataBaseManager(getApplicationContext());
        mLocalData = mSearchDataBaseManager.query();
        initView();
    }

    private void initView() {
        ImageView back_arrow = (ImageView) findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.VISIBLE);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView left_back_title = (TextView) findViewById(R.id.left_back_title);
        left_back_title.setVisibility(View.VISIBLE);
        left_back_title.setText(R.string.category_title);
        left_back_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView middle_text_title = (TextView) findViewById(R.id.middle_text_title);
        middle_text_title.setText(R.string.search_text);


        Button updateButton = (Button) findViewById(R.id.languageButton);
        updateButton.setText(R.string.update_text);
        updateButton.setVisibility(View.VISIBLE);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressTextView.setVisibility(View.VISIBLE);
                new UpdateTask().execute();
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressTextView = (TextView) findViewById(R.id.process_text_view);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        mProductGridAdapter = new ProductGridAdapter(ProductSearchActivity.this, R.layout.product_grid_item, mQureyResult);
        gridView.setAdapter(mProductGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductItem item = mQureyResult.get(position);
                Intent intent = new Intent(ProductSearchActivity.this, ProductImageViewActivity.class);
                intent.putExtra("productItem", item);
                startActivity(intent);
            }
        });
        EditText queryEditText = (EditText) findViewById(R.id.product_search_query);
        queryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mLocalData == null || mLocalData.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Need To Update first!", Toast.LENGTH_SHORT).show();
                }
                mQureyResult.clear();
                mQureyResult.addAll(mSearchDataBaseManager.queryByTitle(s.toString()));
                mProductGridAdapter.notifyDataSetChanged();
            }
        });


    }


    private class UpdateTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<FolderItem> folders = MainApplication.getUpYun()
                    .readDir(MainApplication.RESOURCE_ROOT);
            int total = folders.size() - 1;
            int count = 0;
            if (folders != null) {
                for (FolderItem folderItem : folders) {
                    count++;
                    if (folderItem.type.equals("Folder")) {
                        List<FolderItem> files = MainApplication.getUpYun()
                                .readDir(MainApplication.RESOURCE_ROOT + folderItem.name);
                        if (files != null) {
                            for (FolderItem fileItem : files) {

                                String title = fileItem.name.split("\\.")[0];
                                String image_url = MainApplication.UPYUN_REQUEST_HEADER + folderItem.name
                                        + File.separator + fileItem.name;
                                if (mLocalData != null && mLocalData.size() > 0) {
                                    String value = mLocalData.get(image_url);
                                    if (value != null && !value.equals(title)) {
                                        flag = true;
                                        mSearchDataBaseManager.insert(title, image_url);
                                    }
                                } else {
                                    flag = true;
                                    mSearchDataBaseManager.insert(title, image_url);
                                }
                            }
                        }

                        publishProgress((int) ((count / (float) total) * 100));
                    }
                }
            }
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean isUpdate) {
            super.onPostExecute(isUpdate);
            mProgressBar.setVisibility(View.GONE);
            mProgressTextView.setText("");
            mProgressTextView.setVisibility(View.GONE);
            if (isUpdate) {
                mLocalData.clear();
                mLocalData = mSearchDataBaseManager.query();
                Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Has Synchronized With Server", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            super.onProgressUpdate(progresses);
            mProgressBar.setProgress(progresses[0]);
            mProgressTextView.setText("updating..." + progresses[0] + "%");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchDataBaseManager.close();
    }
}
