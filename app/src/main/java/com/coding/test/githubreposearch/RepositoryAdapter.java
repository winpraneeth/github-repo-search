package com.coding.test.githubreposearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by winpr on 2/6/2018.
 */

class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.MyViewHolder> {

    private ArrayList<Repository> repoList = new ArrayList<>();
    private Context mContext;

    RepositoryAdapter(Context mContext, ArrayList<Repository> repoList) {
        this.repoList = repoList;
        this.mContext = mContext;
    }

    @Override
    public RepositoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_list_item, parent, false);
        return new RepositoryAdapter.MyViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RepositoryAdapter.MyViewHolder holder, int position) {
        Repository repository = repoList.get(position);
        String name = "Name: ";
        String desc = "Description: ";

        Glide.with(mContext).load(repository.getAvatar()).into(holder.avatarImage);
        holder.repoName.setText(name + repository.getRepoName());
        holder.description.setText(desc + repository.getDescription());
        holder.stars.setText(mContext.getString(R.string.star) + String.valueOf(repository.getStars()));
    }

    void setRepoList(ArrayList<Repository> repoList) {
        this.repoList = repoList;
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarImage;
        private TextView repoName;
        private TextView description;
        private TextView stars;

        MyViewHolder(View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.user_image);
            repoName = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            stars = itemView.findViewById(R.id.stars);

        }
    }
}
