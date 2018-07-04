package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Issue;

@Repository
public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

    Issue getByIdAndAccountId(long issueId, long accountId);

    @Query("SELECT issue FROM Issue issue WHERE issue.title LIKE :keyword OR issue.content LIKE :keyword")
    Page<Issue> getAllByTitleLikeOrContentLike(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT issue FROM Issue issue WHERE (issue.title LIKE :keyword OR issue.content LIKE :keyword) AND issue.status = :status ")
    Page<Issue> getAllByTitleLikeOrContentLikeAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

    @Query("FROM Issue issue WHERE issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllByAccountIdAndContentLikeOrTitleLike(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);

    @Query("FROM Issue issue WHERE issue.status = :status AND issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllByAccountIdAndTitleLikeOrContentLikeAndStatus(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            @Param("status") String status,
                                            Pageable pageable);

    Issue findByIdAndToken(long id, String token);

    Issue getById(long issueId);

    boolean existsByIdAndAccountId(long issueId, long accountId);

    void deleteByIdAndAccountId(long issueId, long accountId);
}
