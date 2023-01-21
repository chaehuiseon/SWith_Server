package com.swith.domain.memo.service;

import com.swith.global.error.exception.BaseException;
import com.swith.global.error.ErrorCode;
import com.swith.api.memo.dto.PatchMemoReq;
import com.swith.api.memo.dto.PostMemoReq;
import com.swith.domain.memo.entity.Memo;
import com.swith.domain.session.entity.Session;
import com.swith.domain.user.entity.User;
import com.swith.domain.user.repository.UserRepository;
import com.swith.domain.memo.repository.MemoRepository;
import com.swith.domain.session.repository.SessionRepository;
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
        Long userIdx = postMemoReq.getUserIdx();
        Long sessionIdx = postMemoReq.getSessionIdx();
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_USER));
        Session session = sessionRepository.findById(sessionIdx)
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_SESSION));
        if(memoRepository.existsByUserAndSession(userIdx, sessionIdx))
            throw new BaseException(ErrorCode.ALREADY_EXIST);

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
                .orElseThrow(() -> new BaseException(ErrorCode.INVALID_MEMO));
        memo.setMemoContent(patchMemoReq.getMemoContent());
        Memo savedMemo = memoRepository.save(memo);
        return savedMemo.getMemoIdx();
    }
}
