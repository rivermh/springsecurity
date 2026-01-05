// posts-detail.js

const postRoot = document.getElementById("post-root");
const postId = postRoot?.dataset.postId;

if (!postId) {
    console.error("❌ postId 없음 - data-post-id 확인");
}

const likeBtn = document.getElementById("like-btn");
const likeCountSpan = document.getElementById("like-count");

/* 댓글 로드 */
function loadComments() {
    fetch(`/api/posts/${postId}/comments`)
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById("comment-list");
            list.innerHTML = "";

            data.forEach(comment => {
                const div = document.createElement("div");

                let actionBtns = "";
                if (comment.mine) {
                    actionBtns = `
                        <div class="d-flex gap-1">
                            <button
                                class="btn btn-sm btn-outline-primary edit-btn"
                                data-id="${comment.id}">
                                수정
                            </button>
                            <button
                                class="btn btn-sm btn-outline-danger delete-btn"
                                data-id="${comment.id}">
                                삭제
                            </button>
                        </div>
                    `;
                }

                div.innerHTML = `
                    <div class="card mb-2">
                        <div class="card-body py-2">

                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong>${comment.username}</strong>
                                    <span class="text-muted small ms-2">
                                        ${comment.createdAt}
                                    </span>
                                </div>
                                ${actionBtns}
                            </div>

                            <div class="mt-2 comment-content"
                                 data-id="${comment.id}">
                                ${comment.content}
                            </div>

                        </div>
                    </div>
                `;

                list.appendChild(div);
            });
        });
}

/* 댓글 작성 */
function addComment(e) {
    e.preventDefault();
    const content = document.getElementById("comment-content").value;

    fetch(`/api/posts/${postId}/comments`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ content })
    }).then(res => {
        if (res.ok) {
            document.getElementById("comment-content").value = "";
            loadComments();
        }
    });
}

/* 이벤트 위임 (수정 / 저장 / 취소 / 삭제) */
document.addEventListener("click", e => {

    /* 수정 버튼 */
    if (e.target.classList.contains("edit-btn")) {
        const id = e.target.dataset.id;
        const contentDiv = document.querySelector(
            `.comment-content[data-id="${id}"]`
        );

        const oldContent = contentDiv.textContent.trim();

        contentDiv.innerHTML = `
            <textarea class="form-control mb-2 edit-textarea"
                      rows="3">${oldContent}</textarea>
            <div class="text-end">
                <button class="btn btn-sm btn-primary save-btn"
                        data-id="${id}">
                    저장
                </button>
                <button class="btn btn-sm btn-secondary cancel-btn"
                        data-content="${oldContent}">
                    취소
                </button>
            </div>
        `;
    }

    /* 저장 */
    if (e.target.classList.contains("save-btn")) {
        const id = e.target.dataset.id;
        const contentDiv = e.target.closest(".comment-content");
        const textarea = contentDiv.querySelector(".edit-textarea");
        const newContent = textarea.value.trim();

        if (!newContent) {
            alert("내용을 입력하세요");
            return;
        }

        fetch(`/api/comments/${id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ content: newContent })
        }).then(res => {
            if (res.ok) loadComments();
            else alert("수정 권한 없음");
        });
    }

    /* 취소 */
    if (e.target.classList.contains("cancel-btn")) {
        const oldContent = e.target.dataset.content;
        const contentDiv = e.target.closest(".comment-content");
        contentDiv.textContent = oldContent;
    }

    /* 삭제 */
    if (e.target.classList.contains("delete-btn")) {
        const id = e.target.dataset.id;
        if (!confirm("댓글을 삭제하시겠습니까?")) return;

        fetch(`/api/comments/${id}`, { method: "DELETE" })
            .then(res => {
                if (res.ok) loadComments();
                else alert("삭제 권한 없음");
            });
    }
});

/* 좋아요 */
likeBtn?.addEventListener("click", () => {
    fetch(`/posts/${postId}/like`, { method: "POST" })
        .then(res => res.json())
        .then(data => {
            likeBtn.classList.toggle("btn-danger", data.liked);
            likeBtn.classList.toggle("btn-outline-danger", !data.liked);
            likeCountSpan.textContent = data.likeCount;
        });
});

/* init */
if (postId) {
    document
        .getElementById("comment-form")
        ?.addEventListener("submit", addComment);

    loadComments();
}
