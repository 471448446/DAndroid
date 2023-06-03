package better.library.base.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * Created by better on 2023/6/3 19:32.
 */
public class SimpleHolder<B extends ViewBinding> extends RecyclerView.ViewHolder {
   private B binding;

   public B getBinding() {
      return binding;
   }

   public SimpleHolder(@NonNull B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
