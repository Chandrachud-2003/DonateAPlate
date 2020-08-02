package restaurantapp.randc.com.restaurant_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class SuccessDialog {

    private Activity mActivity;
    private int points;
    private TextView pointsText;
    private LottieAnimationView successAnim;
    private TextView closeButton;
    private int screenWidth;

    private Dialog successDialog;

    public SuccessDialog(Activity activity, int points, int screenWidth)
    {
        this.mActivity = activity;
        this.points = points;
        this.screenWidth = screenWidth;

    }

    public void startDialog()
    {
        successDialog = new Dialog(mActivity);
        successDialog.setContentView(R.layout.success_donation_layout);
        Window window = successDialog.getWindow();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.setCancelable(false);
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        findViewsById();
        successDialog.show();

    }

    private void findViewsById() {

        pointsText = successDialog.findViewById(R.id.pointsText);
        successAnim = successDialog.findViewById(R.id.successAnimation);
        closeButton = successDialog.findViewById(R.id.returnButton);

        successAnim.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                successAnim.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                int newDimensions = (int) (screenWidth*0.5);

                successAnim.getLayoutParams().width = newDimensions;
                successAnim.getLayoutParams().height = newDimensions;

            }
        });

        pointsText.setText(points+ " Points have been Credited to your Account");

        setOnClickListeners();



    }

    private void setOnClickListeners()
    {

        PushDownAnim.setPushDownAnimTo(closeButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       Intent intent = new Intent(mActivity, Main_Activity.class);
                       successAnim.cancelAnimation();
                       mActivity.startActivity(intent);
                       successDialog.dismiss();
                       mActivity.finish();


                    }
                });
    }

}
