package com.project.youthmoa.domain.repository

import com.linecorp.kotlinjdsl.querymodel.jpql.entity.Entities.entity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.project.youthmoa.domain.model.User
import com.project.youthmoa.domain.repository.spec.GetAllUsersSpec
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

interface UserJdslRepository {
    fun findAllBySpec(spec: GetAllUsersSpec): Page<User?>
}

@Repository
class UserJdslRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : UserJdslRepository {
    override fun findAllBySpec(spec: GetAllUsersSpec): Page<User?> {
        val user = entity(User::class)
        return kotlinJdslJpqlExecutor.findPage(spec.pageable) {
            select(user)
                .from(user)
                .whereAnd(
                    spec.roles?.let { path(User::role).`in`(it) },
                    spec.gender?.let { path(User::gender).eq(it) },
                    spec.searchKeyword?.let {
                        path(User::name).like(it).or(
                            path(User::email).like(it).or(
                                path(User::phone).like(it),
                            ),
                        )
                    },
                )
                .orderBy(path(User::createdAt).desc())
        }
    }
}
