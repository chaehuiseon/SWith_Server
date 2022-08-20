package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.dto.request.PatchMemoReq;
import com.example.demo.src.dto.request.PostMemoReq;
import com.example.demo.src.entity.Memo;
import com.example.demo.src.entity.Session;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.MemoRepository;
import com.example.demo.src.repository.SessionRepository;
import com.example.demo.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository, SessionRepository sessionRepository, UserRepository userRepository) {
        this.memoRepository = memoRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Long postMemo(PostMemoReq postMemoReq) throws BaseException {
        User user = userRepository.findById(postMemoReq.getUserIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_USER));
        Session session = sessionRepository.findById(postMemoReq.getSessionIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_SESSION));

        Memo memo = Memo.builder()
                .memoContent(postMemoReq.getMemoContent())
                .session(session)
                .user(user)
                .build();
        Memo postedMemo = memoRepository.save(memo);
        return postedMemo.getMemoIdx();
    }

    public Long patchMemo(PatchMemoReq patchMemoReq) throws BaseException{
        Memo memo = memoRepository.findById(patchMemoReq.getMemoIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_MEMO));
        memo.setMemoContent(patchMemoReq.getMemoContent());
        Memo savedMemo = memoRepository.save(memo);
        return savedMemo.getMemoIdx();
    }
}
