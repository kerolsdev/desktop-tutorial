package com.kerolsmm.incognitopro.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.kerolsmm.incognitopro.Data.MvvmTab;
import com.kerolsmm.incognitopro.Data.Tab;
import com.kerolsmm.incognitopro.Data.TabMvvmModel;
import com.kerolsmm.incognitopro.Data.UpdatePosition;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.Utilts.TabsAdapter;
import com.kerolsmm.incognitopro.databinding.TabsDialogBinding;

import java.util.ArrayList;

public class TabsDialog extends BottomSheetDialogFragment implements TabsAdapter.DeleteTabs {

    private MvvmTab mvvmTab;
    private int mPosition;
    private  ArrayList<Tab> tabs = new ArrayList<>();
    private TabsAdapter tabsAdapter;
    private TabsDialogBinding binding;
    int newPosition ;
    private  Bitmap bitmap;



    public TabsDialog () {

    }

    public TabsDialog (int Position , ArrayList<Tab>tabs , Bitmap bitmap) {
        this.mPosition = Position;
        this.tabs = tabs;
        this.bitmap = bitmap;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabsDialogBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        //this forces the sheet to appear at max height even on landscape

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
    try {
        mvvmTab = new ViewModelProvider(requireActivity()).get(MvvmTab.class);
        newPosition = mPosition;
        tabsAdapter =new TabsAdapter(tabs,this,getActivity(),bitmap);
        binding.ListTabs.setAdapter(tabsAdapter);
        SwipeItem();
        binding.cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.MoreTabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.more_dialog, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
        MaterialCardView Add_tabs = view.findViewById(R.id.Add_tabs);
        Add_tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewAddTab();
            }
        });

        }catch (RuntimeException runtimeException) {

        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private void  NewAddTab () {

        mvvmTab.setDoAnyThing("about:blank");
        dismiss();
    }

    @Override
    public void PositionTabs(int Position) {
        try {
        if (newPosition == Position){
            if (Position >= 1)  {
                tabs.get(Position).getWebView().destroy();
                newPosition = Position - 1;
                tabs.remove(Position);
                tabs.get(newPosition).setSelected(true);
            }else if (Position == 0 && tabs.size() > 1){
                tabs.get(Position).getWebView().destroy();
                tabs.remove(Position);
                newPosition = Position;
                tabs.get(newPosition).setSelected(true);
            }else {
                tabs.get(Position).getWebView().destroy();
                tabs.remove(Position);
            }
            mvvmTab.setArrayListMutableLiveData(new TabMvvmModel(tabs,newPosition));
        }else {
            tabs.get(Position).getWebView().destroy();
            tabs.remove(Position);
            if (newPosition > 0){
                newPosition =  newPosition - 1;
            }
            mvvmTab.setMutableLiveData(new UpdatePosition(tabs.size(),newPosition));
        }
        //tabsAdapter.setArrayList(tabs);
        dismiss();
        }catch (RuntimeException runtimeException) {

        }
    }

    @Override
    public void ClickChangePosition(int Position) {
        try {
                tabs.get(Position).setSelected(true);
                mvvmTab.setArrayListMutableLiveData(new TabMvvmModel(tabs,Position));
                dismiss();
        }catch (RuntimeException runtimeException) {

        }
    }

    private void SwipeItem () {

        try {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,  ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //awesome code when user grabs recycler card to reorder
                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int Position = viewHolder.getAdapterPosition();
                if (newPosition == Position){
                    if (Position >= 1)  {
                        tabs.get(Position).getWebView().destroy();
                        newPosition = Position - 1;
                        tabs.remove(Position);
                        tabs.get(newPosition).setSelected(true);
                    }else if (newPosition == 0 && tabs.size() > 1){
                        tabs.get(Position).getWebView().destroy();
                        tabs.remove(Position);
                        newPosition = Position;
                        tabs.get(newPosition).setSelected(true);
                    }else {
                        tabs.get(Position).getWebView().destroy();
                        tabs.remove(Position);
                    }
                    mvvmTab.setArrayListMutableLiveData(new TabMvvmModel(tabs,newPosition));
                }else {
                    tabs.get(Position).getWebView().destroy();
                    tabs.remove(Position);
                    if (newPosition > 0){
                        newPosition =  newPosition - 1;
                    }
                    mvvmTab.setMutableLiveData(new UpdatePosition(tabs.size(),newPosition));
                }
                //tabsAdapter.setArrayList(tabs);
                dismiss();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.ListTabs);

        }catch (RuntimeException runtimeException ) {

        }
    }
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        try {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        }catch (RuntimeException runtimeException) {


        }

        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

      int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}









