
package com.app.testlauncherapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AndroidLauncherFragment extends Fragment {
    private RecyclerView rv_item;
    private EditText editTextSearch;
    ArrayList<String>app_name=new ArrayList<>();
    private List<String>items=new ArrayList<>();
    private List<String> itemsBack = new ArrayList<>();
    private List<String>itemsMain=new ArrayList<>();
    PackageManager pm;


    public static AndroidLauncherFragment newInstance(){
        return new AndroidLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_android_launcher, container, false);
        rv_item = v.findViewById(R.id.rv_item);
        editTextSearch=v.findViewById(R.id.editTextSearch);
        rv_item.setLayoutManager(new LinearLayoutManager(getActivity()));

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                searchFilter(editable.toString());
            }
        });
        setupAdapter();
        return v;
    }

    private void searchFilter(String searchKeyword) {


        items.clear();

        searchKeyword = searchKeyword.toLowerCase(Locale.getDefault());
        if (searchKeyword.length() == 0) {
            items.addAll(itemsBack);
        }
        else
        {
            for (String s : itemsBack) {
                if (s.toLowerCase()
                        .contains(searchKeyword) )
                {
                    items.add(s);

                }
            }
        }
        filterList(items);

    }

    public void filterList(List<String> filterdNames) {
        this.items = filterdNames;


    }


    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(lhs.loadLabel(pm).toString(),
                        rhs.loadLabel(pm).toString());
            }
        });
        rv_item.setAdapter(new ActivityAdapter(activities));

    }
    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RelativeLayout cvContainer;
        private RelativeLayout fContainer;
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mImageView;
        private TextView mNameLetterTextView;
        String version,appName;


        public ActivityHolder(View itemView) {
            super(itemView);
            cvContainer = itemView.findViewById(R.id.list_item_card_view);
            fContainer = itemView.findViewById(R.id.item_container);
            mNameTextView = itemView.findViewById(R.id.list_item_text_view);
            mImageView = itemView.findViewById(R.id.list_item_image_view);
         //   mNameLetterTextView = itemView.findViewById(R.id.list_item_text_letter);
            cvContainer.setOnClickListener(this);
        }

        public void bindActivity (ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
             pm = getActivity().getPackageManager();
             appName = mResolveInfo.loadLabel(pm).toString();

            Drawable dr =  mResolveInfo.loadIcon(pm);
            mNameTextView.setText(appName);
            mImageView.setImageDrawable(dr);
          //  mNameLetterTextView.setText((""+appName.charAt(0)).toUpperCase());
            itemsBack.addAll(items);
            itemsMain.addAll(itemsBack);



        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;
            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(
                            activityInfo.applicationInfo.packageName, activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder (ViewGroup parent, int type) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent,
                    false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder (ActivityHolder activityHolder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            activityHolder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount () {
            return mActivities.size();
        }
    }


}

