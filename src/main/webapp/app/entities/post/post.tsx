import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Card, CardHeader, CardBody, CardFooter, Heading, Stack, Box, Center, ButtonGroup, Divider, Image, Text } from '@chakra-ui/react';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPost } from 'app/shared/model/post.model';
import { IFlower } from 'app/shared/model/flower.model';
import { getEntities } from './post.reducer';
import { getEntities as getFlowerEntities } from '../flower/flower.reducer';

import './post.scss';

export const Post = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const [postsWithFlower, setPostsWithFlower] = useState<(IPost & { flowers: IFlower[] })[]>([]);

  const postList = useAppSelector<IPost[]>(state => state.post.entities || []);
  const flowerList = useAppSelector<IFlower[]>(state => state.flower.entities || []);
  const loading = useAppSelector(state => state.post.loading);

  useEffect(() => {
    handleSyncList();
  }, []);

  useEffect(() => {
    if (flowerList?.length <= 0 || postList?.length <= 0) return;

    const postsAndFlowers = postList.map(p => {
      const flowers =
        flowerList.filter(flower => {
          return flower.name.split(' ').some(name => {
            return p.name.toLowerCase().includes(name.toLowerCase());
          });
          // return p.name.toLowerCase().includes(flower.name.toLowerCase());
        }) || [];
      const postWithFlower = { ...p, flowers };
      return postWithFlower;
    });

    setPostsWithFlower(postsAndFlowers);
  }, [flowerList, postList]);

  const handleSyncList = () => {
    dispatch(getEntities({}));
    dispatch(getFlowerEntities({}));
  };

  const attachFlowersToPosts = () => {};

  const hasPosts = postList && postList.length > 0;

  return (
    <div>
      {}
      <h2 className="navBarTitleName" id="post-heading" data-cy="PostHeading">
        <Translate contentKey="blogApp.post.home.title">Posts</Translate>
        <div className="d-flex justify-content-end">
          <Link to="/post/new" id="jh-create-entity" data-cy="entityCreateButton">
            <Button className="createNewPostButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="blogApp.post.home.createLabel">Create new Post</Translate>
            </Button>
          </Link>
        </div>
      </h2>
      {hasPosts ? (
        <>
          {postsWithFlower.map(({ name, date, user, id, flowers }, i) => (
            <Card maxW="sm" key={i}>
              <Center bg="tomato" h="100px" color="white">
                <Heading size="md">{name}</Heading>
              </Center>
              <CardBody>
                {flowers.map((flower, j) => {
                  return (
                    <Stack key={j}>
                      {flower.imageLink && <Image src={flower.imageLink} alt={flower.name} borderRadius="lg" />}
                      <Heading size="md">{flower.name}</Heading>
                      <Text>{flower.description}</Text>
                    </Stack>
                  );
                })}
              </CardBody>
              <Divider />
              <CardFooter>
                <ButtonGroup spacing="2">
                  <Button tag={Link} to={`/post/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`/post/${id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`/post/${id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                    <FontAwesomeIcon icon="trash" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.delete">Delete</Translate>
                    </span>
                  </Button>
                </ButtonGroup>
              </CardFooter>
            </Card>
          ))}
        </>
      ) : (
        !loading && (
          <div className="alert alert-warning">
            <Translate contentKey="blogApp.post.home.notFound">No Posts found</Translate>
          </div>
        )
      )}
    </div>
  );
};

export default Post;
