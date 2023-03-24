import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Container, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  Card,
  CardHeader,
  CardBody,
  CardFooter,
  Heading,
  Stack,
  Box,
  Center,
  ButtonGroup,
  Divider,
  Image,
  Text,
  Flex,
  SimpleGrid,
  Spacer,
} from '@chakra-ui/react';

import './post.scss';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPost } from 'app/shared/model/post.model';
import { getEntities } from './post.reducer';
import FlowerCard from '../flower/flower-card';

export const Post = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const postList = useAppSelector<IPost[]>(state => state.post.entities);
  const loading = useAppSelector(state => state.post.loading);

  useEffect(() => {
    dispatch(getEntities({ query: 'eagerload=true' }));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <Flex justifyContent={'center'} alignItems="center">
        <Box flex={1}></Box>
        <Center fontFamily="Alegreya" justifyContent={'center'} flex={1}>
          <h2 className="postHeading" data-cy="PostHeading">
            <Translate contentKey="blogApp.post.home.title">Posts</Translate>
          </h2>
        </Center>
        <ButtonGroup justify-content={'flex-end'} spacing="2" flex="1" flexBasis="0">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="blogApp.post.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button tag={Link} to={`/post/new`}>
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="blogApp.post.home.createLabel">Create new Post</Translate>
          </Button>
        </ButtonGroup>
      </Flex>

      <Container mt={12} font-Family="Alegreya">
        <Flex flexWrap="wrap" gridGap={2} justify="center">
          {postList?.map(({ name, date, user, id, flowers }, i) => (
            <Card w="85vw" key={i} padding="4" margin={4}>
              <Flex bg="#f4d2d2" color="black" justifyContent="space-between" p="4">
                <Box flexGrow="1" flexBasis="0"></Box>
                <Heading size="lg" fontFamily="Alegreya">
                  {name}
                </Heading>
                <ButtonGroup justifyContent={'flex-end'} spacing="2" flexGrow="1" flexBasis="0">
                  {/* <Button tag={Link} to={`/post/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
                  <FontAwesomeIcon icon="eye" />{' '}
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.view">View</Translate>
                  </span>
                </Button> */}
                  <Button tag={Link} to={`/post/${id}/edit`} color="white" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    {/* <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.edit">Edit</Translate>
                      </span> */}
                  </Button>
                  <Button tag={Link} to={`/post/${id}/delete`} size="sm" color="white" data-cy="entityDeleteButton">
                    <FontAwesomeIcon icon="trash" />{' '}
                    {/* <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.delete">Delete</Translate>
                      </span> */}
                  </Button>
                </ButtonGroup>
              </Flex>
              <CardBody>
                <Flex justify="space-evenly" alignItems="center">
                  {flowers?.map(flower => (
                    <Link to={`/flower/${flower.id}`}>
                      <FlowerCard key={flower.id} {...{ flower }} />
                    </Link>
                  ))}
                </Flex>
              </CardBody>
              {/* <Divider /> */}
              <Flex justify="center" alignItems={'center'}>
                <CardFooter></CardFooter>
              </Flex>
            </Card>
          ))}
        </Flex>
      </Container>
      {/* <div className="table-responsive">
        {postList && postList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="blogApp.post.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.post.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.post.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.post.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="blogApp.post.flower">Flower</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {postList.map((post, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/post/${post.id}`} color="link" size="sm">
                      {post.id}
                    </Button>
                  </td>
                  <td>{post.name}</td>
                  <td>{post.date}</td>
                  <td>{post.user ? post.user.login : ''}</td>
                  <td>
                    {post.flowers
                      ? post.flowers.map((val, j) => (
                          <span key={j}>
                            <Link to={`/flower/${val.id}`}>{val.name}</Link>
                            {j === post.flowers.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/post/${post.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/post/${post.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/post/${post.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table> */}
      {/* // ) : (
        //   !loading && (
        //     <div className="alert alert-warning">
        //       <Translate contentKey="blogApp.post.home.notFound">No Posts found</Translate>
        //     </div>
        //   )
        // )}
      </div> */}
    </div>
  );
};

export default Post;
