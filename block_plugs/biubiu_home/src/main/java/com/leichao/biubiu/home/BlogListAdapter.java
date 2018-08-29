package com.leichao.biubiu.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leichao.util.ToastUtil;

import java.util.List;

public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.BlogListHolder> {

    private Context context;
    private List<Integer> beanList;

    public BlogListAdapter(Context context, List<Integer> blogList) {
        this.context = context;
        this.beanList = blogList;
    }

    @NonNull
    @Override
    public BlogListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_list, parent, false);
        return new BlogListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogListHolder holder, final int position) {
        final int bean = beanList.get(position);
        if (bean > 0) {
            holder.tvAppName.setVisibility(View.VISIBLE);
            holder.tvAppName.setText(String.valueOf(bean));
            holder.tvAppName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(String.valueOf(bean));
                }
            });
        } else {
            holder.tvAppName.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class BlogListHolder extends RecyclerView.ViewHolder {

        private TextView tvAppName;

        BlogListHolder(View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.home_app_name);
        }
    }

}
