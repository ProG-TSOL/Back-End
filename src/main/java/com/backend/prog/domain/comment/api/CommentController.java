package com.backend.prog.domain.comment.api;

import com.backend.prog.domain.comment.application.CommentService;
import com.backend.prog.domain.comment.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public void createComment(@RequestBody CommentDto.Post post) {
        commentService.createComment(post);
    }

    @GetMapping
    public Object getParentComments(@RequestParam(name = "memberId", required = false) Integer memberId,
                                    @RequestParam("contentCode") String contentCode,
                                    @RequestParam("contentId") Long contentId) {
        return commentService.getParentComments(memberId, contentCode, contentId);
    }

    @GetMapping("/{parent-id}/children")
    public Object getChildrenComments(@PathVariable("parent-id") Long parentId) {
        return commentService.getChildrenComments(parentId);
    }

    @PatchMapping("/{comment-id}/{member-id}")
    public void updateComment(@PathVariable("comment-id") Long commentId,
                              @PathVariable("member-id") Integer memberId,
                              @RequestBody CommentDto.Patch patch) {
        commentService.updateComment(commentId, memberId, patch);
    }

    @DeleteMapping("/{comment-id}/{member-id}")
    public void deleteComment(@PathVariable("comment-id") Long commentId, @PathVariable("member-id") Integer memberId) {
        commentService.deleteComment(commentId, memberId);
    }
}
