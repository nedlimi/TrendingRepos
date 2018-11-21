package com.nuguru.gitreposearch.Frames;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.nuguru.gitreposearch.GitAdapter;
import com.nuguru.gitreposearch.GithubNetwork.Network;
import com.nuguru.gitreposearch.POJO.Repo;
import com.nuguru.gitreposearch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private String TAG = HomeFragment.class.getSimpleName();
    private List<Repo> RepositoryList ;

    private RecyclerView mRecyclerView;
    private GitAdapter mGitAdapter;
    private ProgressBar mLoadingIndicator;

    Boolean isScrollng = false;
    int currentItems , totalItems,scrollOutItems;
    int pageNumber = 1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_jsdata);

        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mGitAdapter = new GitAdapter();
        mRecyclerView.setAdapter(mGitAdapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecor);

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.load_indicator);

        LoadData(pageNumber);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollng = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (isScrollng && (currentItems + scrollOutItems == totalItems)){
                    isScrollng = false;
                    LoadData(pageNumber++);
                }

            }
        });


        return view;
    }

    private void LoadData(int p){

        String pageNum = Integer.toString(p);
        URL githubSearchUrl = Network.buildUrl(pageNum);

        new HomeFragment.GithubQueryTask().execute(githubSearchUrl);
    }
    private void showJsonDataView() {

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, List<Repo>> {

        private Repo mRepository;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List doInBackground(URL... urls) {

            if (urls.length == 0) {
                return null;
            }

            URL searchUrl = urls[0];
            String githubUrlSearchResults = null;
            try {
                githubUrlSearchResults = Network.getResponseFromHttpUrl(searchUrl);
                if(githubUrlSearchResults !=null) {

                    JSONObject jsonObj = new JSONObject(githubUrlSearchResults);
                    JSONArray items = jsonObj.getJSONArray("items");
                    RepositoryList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        mRepository = new Repo();
                        JSONObject mJsonObj = items.getJSONObject(i);
                        mRepository.setName( mJsonObj.getString("name"));
                        mRepository.setDescription(mJsonObj.getString("description"));

                        JSONObject owner = mJsonObj.getJSONObject("owner");
                        mRepository.setOwnerName(owner.getString("login"));
                        mRepository.setOwnerAvatar(owner.getString("avatar_url"));
                        mRepository.setRatings(mJsonObj.getString("stargazers_count"));
                        RepositoryList.add(mRepository);
                    }
                }else {
                    Log.e(TAG, "No Data found in the server");
                }
                return RepositoryList;



            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List githubSearchResults) {
            super.onPostExecute(githubSearchResults);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {

                showJsonDataView();
                mGitAdapter.setmRepoData(githubSearchResults);
            } else {
                Log.d(TAG,"GithubSearchResult is null");
            }
        }

    }

}
