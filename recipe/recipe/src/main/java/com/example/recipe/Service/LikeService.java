package com.example.recipe.Service;

import com.example.recipe.Entity.Like;
import com.example.recipe.Entity.User;
import com.example.recipe.Entity.Recipe;
import com.example.recipe.Repository.LikeRepository;
import com.example.recipe.Repository.RecipeRepository;
import com.example.recipe.Repository.UserRepository;
import com.example.recipe.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public void addLike(String userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Like like = new Like();
        like.setUser(user);
        like.setRecipe(recipe);
        likeRepository.save(like);
    }

    public void removeLike(String userId, Long recipeId) {
        Like like = likeRepository.findByUserUserIdAndRecipeRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        likeRepository.delete(like);
    }

    public boolean checkLikeExists(String userId, Long recipeId) { //좋아요를 눌렀는지 확이하는 메소드
        return likeRepository.existsByUserUserIdAndRecipeRecipeId(userId, recipeId);
    }

    public List<Like> getLikesByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        // 좋아요 엔티티에 사용자 필드가 있으므로 해당 사용자가 좋아요한 모든 레시피 가져오기
        return likeRepository.findAllByUserUserId(userId);
    }
}
