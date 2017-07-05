package com.mingchu.common.face;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingchu.common.R;


public class FaceAdapter extends RecyclerView.Adapter<FaceHolder> implements View.OnClickListener {
    private Face.FaceTab faceTab;
    private FaceListener listener;

    public FaceAdapter(Face.FaceTab faceTab, FaceListener listener) {
        this.faceTab = faceTab;
        this.listener = listener;
    }

    @Override
    public FaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.cell_face, parent, false);
        root.setOnClickListener(this);
        return new FaceHolder(root);
    }

    @Override
    public void onBindViewHolder(FaceHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.set(faceTab.faces.get(position));
    }

    @Override
    public int getItemCount() {
        return faceTab.faces.size();
    }

    @Override
    public void onClick(View v) {
        listener.onFaceClick(faceTab.faces.get((Integer) v.getTag()));
    }
}
