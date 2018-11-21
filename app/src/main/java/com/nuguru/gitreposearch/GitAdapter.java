package com.nuguru.gitreposearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuguru.gitreposearch.POJO.Repo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GitAdapter extends RecyclerView.Adapter<GitAdapter.GitViewholder> {

    private List<Repo> mRepoData;

    public GitAdapter(){

    }
    @NonNull
    @Override
    public GitViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GitViewholder(view);
    }

    @Override
    public void onBindViewHolder(GitViewholder gitViewholder, int i) {
        Repo mRepo = mRepoData.get(i);
        gitViewholder.mRepoNameView.setText(mRepo.getName());
        gitViewholder.mRepoDescView.setText(mRepo.getDescription());
        gitViewholder.mRepoOwnerView.setText(mRepo.getOwnerName());
        Picasso.get().load(mRepo.getOwnerAvatar())
                .error(R.drawable.ic_image_black_24dp)
                .resize(50,50).
                into(gitViewholder.mOwnerAvatarView);
        gitViewholder.mRepoRatingView.setText(mRepo.getRatings());

    }

    @Override
    public int getItemCount() {
        if(mRepoData == null)
        return 0;
        else return mRepoData.size();
    }

    public void setmRepoData(List data) {
        mRepoData = data;
        notifyDataSetChanged();
    }

    public class GitViewholder extends RecyclerView.ViewHolder{

        public TextView mRepoNameView;
        public TextView mRepoDescView;
        public TextView mRepoOwnerView;
        public TextView mRepoRatingView;
        public ImageView mOwnerAvatarView;

        public GitViewholder(View itemView) {
            super(itemView);
            mRepoNameView = (TextView) itemView.findViewById(R.id.rv_repo_name);
            mRepoDescView = (TextView) itemView.findViewById(R.id.rv_repo_descr);
            mRepoOwnerView = (TextView) itemView.findViewById(R.id.rv_repo_owner);
            mRepoRatingView = (TextView) itemView.findViewById(R.id.rv_repo_rating);
            mOwnerAvatarView = (ImageView) itemView.findViewById(R.id.rv_owner_avatar);
        }
    }
}
